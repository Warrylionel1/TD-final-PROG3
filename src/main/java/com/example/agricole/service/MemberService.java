package com.example.agricole.service;

import com.example.agricole.dto.CreateMember;
import com.example.agricole.dto.CreateMemberPayment;
import com.example.agricole.entity.*;
import com.example.agricole.exception.BadRequestException;
import com.example.agricole.exception.CollectivityNotFoundException;
import com.example.agricole.exception.MemberNotFoundException;
import com.example.agricole.repository.*;
import com.example.agricole.validator.MemberExperienceValidator;
import com.example.agricole.validator.MemberRefereeValidator;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final CollectivityRepository collectivityRepository;
    private final MemberRefereeValidator memberRefereeValidator;
    @Getter
    private final MemberExperienceValidator memberExperienceValidator;
    private final MembershipFeeRepository membershipFeeRepository;
    private final FinancialAccountRepository financialAccountRepository;
    private final PaymentRepository paymentRepository;
    private final TransactionRepository transactionRepository;

    public MemberService(MemberRepository memberRepository,
                         CollectivityRepository collectivityRepository,
                         MemberRefereeValidator memberRefereeValidator,
                         MemberExperienceValidator memberExperienceValidator,
                         MembershipFeeRepository membershipFeeRepository,
                         FinancialAccountRepository financialAccountRepository,
                         PaymentRepository paymentRepository,
                         TransactionRepository transactionRepository) {
        this.memberRepository = memberRepository;
        this.collectivityRepository = collectivityRepository;
        this.memberRefereeValidator = memberRefereeValidator;
        this.memberExperienceValidator = memberExperienceValidator;
        this.membershipFeeRepository = membershipFeeRepository;
        this.financialAccountRepository = financialAccountRepository;
        this.paymentRepository = paymentRepository;
        this.transactionRepository = transactionRepository;
    }

    public List<Member> createMembers(List<CreateMember> requests) {
        List<Member> created = new ArrayList<>();

        for (CreateMember req : requests) {
            Collectivity collectivity = collectivityRepository.findById(req.getCollectivityIdentifier());
            if (collectivity == null) {
                throw new CollectivityNotFoundException(req.getCollectivityIdentifier());
            }

            if (!req.isRegistrationFeePaid() || !req.isMembershipDuesPaid()) {
                throw new BadRequestException("Payment not valid");
            }
            if (req.getReferees() == null || req.getReferees().isEmpty()) {
                throw new BadRequestException("Referees required");
            }

            List<Member> referees = memberRepository.findByIds(req.getReferees());
            if (referees.size() != req.getReferees().size()) {
                throw new MemberNotFoundException("One or more referees not found");
            }

            memberRefereeValidator.validate(referees, collectivity);

            Member member = new Member();
            member.setId(UUID.randomUUID().toString());
            member.setFirstName(req.getFirstName());
            member.setLastName(req.getLastName());
            member.setBirthDate(req.getBirthDate());
            member.setGender(req.getGender());
            member.setAddress(req.getAddress());
            member.setProfession(req.getProfession());
            member.setPhoneNumber(req.getPhoneNumber());
            member.setEmail(req.getEmail());
            member.setOccupation(req.getOccupation());
            member.setJoinDate(LocalDate.now());
            member.setReferees(referees);

            memberRepository.save(member);
            created.add(member);
        }
        return created;
    }

    public List<MemberPayment> createPayments(String memberId, List<CreateMemberPayment> payments) {
        Member member = memberRepository.findById(memberId);
        if (member == null) throw new MemberNotFoundException("Member not found: " + memberId);

        List<MemberPayment> results = new ArrayList<>();
        for (CreateMemberPayment dto : payments) {
            MembershipFee fee = membershipFeeRepository.findById(dto.getMembershipFeeIdentifier());
            if (fee == null) {
                throw new BadRequestException("Membership fee not found: " + dto.getMembershipFeeIdentifier());
            }

            FinancialAccount account = financialAccountRepository.findById(dto.getAccountCreditedIdentifier())
                    .orElseThrow(() -> new BadRequestException("Financial account not found: " + dto.getAccountCreditedIdentifier()));

            double newAmount = account.getAmount() + dto.getAmount();
            financialAccountRepository.updateAmount(account.getId(), newAmount);
            account = financialAccountRepository.findById(account.getId()).orElse(account);

            MemberPayment payment = new MemberPayment();
            payment.setMemberId(memberId);
            payment.setMembershipFeeId(fee.getId());
            payment.setAmount(dto.getAmount());
            payment.setPaymentMode(dto.getPaymentMode());
            payment.setAccountCredited(account);
            payment.setCreationDate(LocalDate.now());
            payment = paymentRepository.save(payment);

            CollectivityTransaction tx = new CollectivityTransaction();
            tx.setCollectivityId(account.getCollectivityId());
            tx.setCreationDate(LocalDate.now());
            tx.setAmount(dto.getAmount());
            tx.setPaymentMode(dto.getPaymentMode());
            tx.setAccountCredited(account);
            tx.setMemberDebited(member);
            transactionRepository.save(tx);

            results.add(payment);
        }
        return results;
    }
}