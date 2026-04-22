package com.example.agricole.service;

import com.example.agricole.dto.CreateMember;
import com.example.agricole.entity.Collectivity;
import com.example.agricole.entity.Member;
import com.example.agricole.exception.BadRequestException;
import com.example.agricole.exception.CollectivityNotFoundException;
import com.example.agricole.repository.CollectivityRepository;
import com.example.agricole.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final CollectivityRepository collectivityRepository;

    public MemberService(MemberRepository memberRepository,
                         CollectivityRepository collectivityRepository) {
        this.memberRepository = memberRepository;
        this.collectivityRepository = collectivityRepository;
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

            List<Member> referees = memberRepository.findByIds(req.getReferees());

            if (referees.size() != req.getReferees().size()) {
                throw new BadRequestException("Some referees not found");
            }

            validateReferees(referees, collectivity);

            Member member = new Member();
            member.setFirstName(req.getFirstName());
            member.setLastName(req.getLastName());
            member.setEmail(req.getEmail());
            member.setOccupation(req.getOccupation());
            member.setJoinDate(LocalDate.now());

            memberRepository.save(member);

            created.add(member);
        }

        return created;
    }

    private void validateReferees(List<Member> referees, Collectivity collectivity) {

        if (referees.size() < 2) {
            throw new BadRequestException("At least 2 referees required");
        }

        long internal = referees.stream()
                .filter(r -> isMemberInCollectivity(r, collectivity))
                .count();

        long external = referees.size() - internal;

        if (internal < external) {
            throw new BadRequestException("Invalid referees distribution");
        }
    }

    private boolean isMemberInCollectivity(Member member, Collectivity collectivity) {

        if (collectivity.getMembers() == null) {
            return false;
        }

        return collectivity.getMembers()
                .stream()
                .anyMatch(m -> m.getId().equals(member.getId()));
    }
}