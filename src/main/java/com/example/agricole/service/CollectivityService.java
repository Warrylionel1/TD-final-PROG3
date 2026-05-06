package com.example.agricole.service;

import com.example.agricole.dto.*;
import com.example.agricole.entity.*;
import com.example.agricole.enums.ActivityStatus;
import com.example.agricole.exception.*;
import com.example.agricole.repository.*;
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
    private final StatisticsService statisticsService;

    public CollectivityService(
            CollectivityRepository collectivityRepository,
            MemberRepository memberRepository,
            MemberExperienceValidator experienceValidator,
            CollectivityStructureValidator structureValidator,
            MembershipFeeRepository membershipFeeRepository,
            TransactionRepository transactionRepository,
            FinancialAccountRepository financialAccountRepository,
            StatisticsService statisticsService
    ) {
        this.collectivityRepository = collectivityRepository;
        this.memberRepository = memberRepository;
        this.experienceValidator = experienceValidator;
        this.structureValidator = structureValidator;
        this.membershipFeeRepository = membershipFeeRepository;
        this.transactionRepository = transactionRepository;
        this.financialAccountRepository = financialAccountRepository;
        this.statisticsService = statisticsService;
    }

    private Collectivity getCollectivityOrThrow(String id) {
        Collectivity collectivity = collectivityRepository.findById(id);

        if (collectivity == null) {
            throw new CollectivityNotFoundException(id);
        }

        return collectivity;
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

            collectivityRepository.saveWithRelations(
                    collectivity,
                    req.getStructure()
            );

            CashAccount cashAccount = new CashAccount();
            cashAccount.setCollectivityId(collectivity.getId());
            cashAccount.setAmount(0);

            financialAccountRepository.save(cashAccount);

            Collectivity saved = getCollectivityOrThrow(collectivity.getId());
            result.add(saved);
        }

        return result;
    }

    public Collectivity assignIdentity(String collectivityId, AssignIdentityRequest request) {

        Collectivity collectivity = getCollectivityOrThrow(collectivityId);

        if (collectivity.hasAssignedIdentity()) {
            throw new ConflictException("Identity already assigned");
        }

        if (collectivityRepository.existsByNumber(request.getNumber())) {
            throw new ConflictException("Number already used");
        }

        if (collectivityRepository.existsByName(request.getName())) {
            throw new ConflictException("Name already used");
        }

        collectivityRepository.updateNumberAndName(
                collectivityId,
                request.getNumber(),
                request.getName()
        );

        return getCollectivityOrThrow(collectivityId);
    }

    public List<MembershipFee> getMembershipFees(String collectivityId) {

        getCollectivityOrThrow(collectivityId);

        return membershipFeeRepository.findByCollectivityId(collectivityId);
    }

    public List<MembershipFee> createMembershipFees(
            String collectivityId,
            List<CreateMembershipFee> requests) {

        getCollectivityOrThrow(collectivityId);

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

    public List<CollectivityTransaction> getTransactions(
            String collectivityId,
            LocalDate from,
            LocalDate to) {

        if (from == null || to == null) {
            throw new BadRequestException("from and to are required");
        }

        getCollectivityOrThrow(collectivityId);

        return transactionRepository
                .findByCollectivityIdAndDateRange(collectivityId, from, to);
    }

    public Collectivity getCollectivityById(String id) {

        Collectivity c = getCollectivityOrThrow(id);
        c.setMembers(memberRepository.findByCollectivityId(id));

        return c;
    }

    public List<CollectivityLocalStatistics> getLocalStatistics(
            String collectivityId,
            LocalDate from,
            LocalDate to) {

        getCollectivityOrThrow(collectivityId);

        return statisticsService.getLocalStatistics(collectivityId, from, to);
    }

    public List<CollectivityOverallStatistics> getOverallStatistics(
            LocalDate from,
            LocalDate to) {

        return statisticsService.getOverallStatistics(from, to);
    }

    public List<FinancialAccount> getFinancialAccounts(
            String collectivityId,
            LocalDate at) {

        getCollectivityOrThrow(collectivityId);

        return financialAccountRepository
                .findAllByCollectivityIdWithBalanceAt(collectivityId, at);
    }
}