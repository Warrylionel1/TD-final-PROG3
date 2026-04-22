package com.example.agricole.service;

import com.example.agricole.dto.CreateMember;
import com.example.agricole.entity.Collectivity;
import com.example.agricole.entity.Member;
import com.example.agricole.exception.BadRequestException;
import com.example.agricole.exception.CollectivityNotFoundException;
import com.example.agricole.exception.MemberNotFoundException;
import com.example.agricole.repository.CollectivityRepository;
import com.example.agricole.repository.MemberRepository;
import com.example.agricole.validator.MemberExperienceValidator;
import com.example.agricole.validator.MemberRefereeValidator;
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
    private final MemberExperienceValidator memberExperienceValidator;

    public MemberService(MemberRepository memberRepository,
                         CollectivityRepository collectivityRepository,
                         MemberRefereeValidator memberRefereeValidator,
                         MemberExperienceValidator memberExperienceValidator) {

        this.memberRepository = memberRepository;
        this.collectivityRepository = collectivityRepository;
        this.memberRefereeValidator = memberRefereeValidator;
        this.memberExperienceValidator = memberExperienceValidator;
    }

    public List<Member> createMembers(List<CreateMember> requests) {

        List<Member> created = new ArrayList<>();

        for (CreateMember req : requests) {

            Collectivity collectivity =
                    collectivityRepository.findById(req.getCollectivityIdentifier());

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
            member.setEmail(req.getEmail());
            member.setOccupation(req.getOccupation());
            member.setJoinDate(LocalDate.now());
            member.setReferees(referees);

            memberRepository.save(member);

            created.add(member);
        }

        return created;
    }
}