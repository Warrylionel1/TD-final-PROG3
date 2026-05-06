package com.example.agricole.service;

import com.example.agricole.dto.AssignIdentityRequest;
import com.example.agricole.dto.CollectivityInformation;
import com.example.agricole.dto.CollectivityLocalStatistics;
import com.example.agricole.dto.CollectivityOverallStatistics;
import com.example.agricole.dto.CreateCollectivity;
import com.example.agricole.dto.CreateMembershipFee;
import com.example.agricole.entity.*;
import com.example.agricole.enums.ActivityStatus;
import com.example.agricole.exception.BadRequestException;
import com.example.agricole.exception.CollectivityNotFoundException;
import com.example.agricole.exception.ConflictException;
import com.example.agricole.exception.MemberNotFoundException;
import com.example.agricole.repository.CollectivityRepository;
import com.example.agricole.repository.FinancialAccountRepository;
import com.example.agricole.repository.MemberRepository;
import com.example.agricole.repository.MembershipFeeRepository;
import com.example.agricole.repository.StatisticsRepository;
import com.example.agricole.repository.TransactionRepository;
import com.example.agricole.validator.CollectivityStructureValidator;
import com.example.agricole.validator.MemberExperienceValidator;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CollectivityService {

    private final CollectivityRepository collectivityRepository;
    private final MemberRepository memberRepository;
    private final MemberExperienceValidator experienceValidator;
    private final CollectivityStructureValidator structureValidator;
    private final MembershipFeeRepository membershipFeeRepository;
    private final TransactionRepository transactionRepository;
    private final FinancialAccountRepository financialAccountRepository;
    private final StatisticsRepository statisticsRepository;

    public CollectivityService(
            CollectivityRepository collectivityRepository,
            MemberRepository memberRepository,
            MemberExperienceValidator experienceValidator,
            CollectivityStructureValidator structureValidator,
            MembershipFeeRepository membershipFeeRepository,
            TransactionRepository transactionRepository,
            FinancialAccountRepository financialAccountRepository,
            StatisticsRepository statisticsRepository
    ) {
        this.collectivityRepository = collectivityRepository;
        this.memberRepository = memberRepository;
        this.experienceValidator = experienceValidator;
        this.structureValidator = structureValidator;
        this.membershipFeeRepository = membershipFeeRepository;
        this.transactionRepository = transactionRepository;
        this.financialAccountRepository = financialAccountRepository;
        this.statisticsRepository = statisticsRepository;
    }

    public List<Collectivity> createCollectivities(List<CreateCollectivity> requests) {
        List<Collectivity> result = new ArrayList<>();

        for (CreateCollectivity req : requests) {
            if (!req.isFederationApproval()) {
                throw new BadRequestException("Federation approval required");
            }
            if (req.getStructure() == null) {
                throw new BadRequestException("Structure is required");
            }

            List<Member> members = memberRepository.findByIds(req.getMembers());
            if (members.size() != req.getMembers().size()) {
                throw new MemberNotFoundException("Some members not found");
            }
            if (members.size() < 10) {
                throw new BadRequestException("Minimum 10 members required");
            }

            long experiencedMembers = members.stream()
                    .filter(experienceValidator::has6MonthsExperience)
                    .count();
            if (experiencedMembers < 5) {
                throw new BadRequestException("At least 5 experienced members required");
            }

            structureValidator.validateStructure(req, members);

            Collectivity collectivity = new Collectivity();
            collectivity.setId(UUID.randomUUID().toString());
            collectivity.setLocation(req.getLocation());
            collectivity.setMembers(members);
            collectivity.setFederationApproval(true);

            collectivityRepository.saveWithRelations(collectivity, req.getStructure());

            CashAccount cashAccount = new CashAccount();
            cashAccount.setCollectivityId(collectivity.getId());
            cashAccount.setAmount(0);
            financialAccountRepository.save(cashAccount);

            Collectivity saved = collectivityRepository.findById(collectivity.getId());
            result.add(saved);
        }
        return result;
    }

    public Collectivity assignIdentity(String collectivityId, AssignIdentityRequest request) {
        Collectivity collectivity = collectivityRepository.findById(collectivityId);
        if (collectivity == null) {
            throw new CollectivityNotFoundException(collectivityId);
        }
        if (collectivity.hasAssignedIdentity()) {
            throw new ConflictException("This collectivity has already a number and a name");
        }
        if (collectivityRepository.existsByNumber(request.getNumber())) {
            throw new ConflictException("Number " + request.getNumber() + " is already used");
        }
        if (collectivityRepository.existsByName(request.getName())) {
            throw new ConflictException("Name " + request.getName() + " is already used");
        }
        collectivityRepository.updateNumberAndName(collectivityId, request.getNumber(), request.getName());
        return collectivityRepository.findById(collectivityId);
    }

    public List<MembershipFee> getMembershipFees(String collectivityId) {
        Collectivity col = collectivityRepository.findById(collectivityId);
        if (col == null) throw new CollectivityNotFoundException(collectivityId);
        return membershipFeeRepository.findByCollectivityId(collectivityId);
    }

    public List<MembershipFee> createMembershipFees(String collectivityId, List<CreateMembershipFee> requests) {
        Collectivity col = collectivityRepository.findById(collectivityId);
        if (col == null) throw new CollectivityNotFoundException(collectivityId);
        List<MembershipFee> saved = new ArrayList<>();
        for (CreateMembershipFee dto : requests) {
            if (dto.getAmount() < 0) {
                throw new BadRequestException("Amount cannot be negative");
            }
            MembershipFee fee = new MembershipFee();
            fee.setCollectivityId(collectivityId);
            fee.setEligibleFrom(dto.getEligibleFrom());
            fee.setFrequency(dto.getFrequency());
            fee.setAmount(dto.getAmount());
            fee.setLabel(dto.getLabel());
            fee.setStatus(ActivityStatus.ACTIVE);
            saved.add(membershipFeeRepository.save(fee));
        }
        return saved;
    }

    public List<CollectivityTransaction> getTransactions(String collectivityId, LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new BadRequestException("Query parameters 'from' and 'to' are mandatory");
        }
        Collectivity col = collectivityRepository.findById(collectivityId);
        if (col == null) throw new CollectivityNotFoundException(collectivityId);
        return transactionRepository.findByCollectivityIdAndDateRange(collectivityId, from, to);
    }

    public Collectivity getCollectivityById(String id) {
        Collectivity c = collectivityRepository.findById(id);
        if (c == null) {
            throw new RuntimeException("Collectivity not found");
        }
        List<Member> members = memberRepository.findByCollectivityId(id);
        c.setMembers(members);
        return c;
    }

    public List<CollectivityLocalStatistics> getLocalStatistics(
            String collectivityId, LocalDate from, LocalDate to) {
        if (collectivityRepository.findById(collectivityId) == null) {
            throw new CollectivityNotFoundException(collectivityId);
        }
        return statisticsRepository.getLocalStatistics(collectivityId, from, to);
    }

    public List<CollectivityOverallStatistics> getOverallStatistics(
            LocalDate from, LocalDate to) {
        List<Collectivity> all = collectivityRepository.findAll();
        List<CollectivityOverallStatistics> result = new ArrayList<>();

        for (Collectivity c : all) {
            String colId = c.getId();

            int newMembers = (int) statisticsRepository.countNewMembers(colId, from, to);
            double totalRequired = statisticsRepository.totalActiveRequired(colId, to);
            long upToDate = statisticsRepository.countUpToDateMembers(colId, to, totalRequired);

            List<Member> members = memberRepository.findByCollectivityId(colId);
            double percent = members.isEmpty() ? 0.0 : (upToDate * 100.0) / members.size();

            result.add(new CollectivityOverallStatistics(
                    new CollectivityInformation(c.getNumber(), c.getName()),
                    newMembers,
                    percent
            ));
        }
        return result;
    }
}