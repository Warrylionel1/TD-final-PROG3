package com.example.agricole.validator;

import com.example.agricole.dto.CreateMember;
import com.example.agricole.entity.Collectivity;
import com.example.agricole.entity.Member;
import com.example.agricole.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ValidationService {

    private final MemberRepository memberRepository;

    public ValidationService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public boolean isMemberInCollectivity(Member member, Collectivity collectivity) {

        if (collectivity == null || collectivity.getMembers() == null) {
            return false;
        }

        return collectivity.getMembers()
                .stream()
                .anyMatch(m -> m.getId().equals(member.getId()));
    }

    public boolean has6MonthsExperience(Member member) {

        if (member == null || member.getJoinDate() == null) {
            return false;
        }

        return member.getJoinDate()
                .isBefore(LocalDate.now().minusMonths(6));
    }

    public void validateReferees(CreateMember req, Collectivity collectivity) {

        List<Member> referees = memberRepository.findByIds(req.getReferees());

        if (referees == null || referees.size() < 2) {
            throw new RuntimeException("At least 2 referees required");
        }

        long internal = referees.stream()
                .filter(r -> isMemberInCollectivity(r, collectivity))
                .count();

        long external = referees.size() - internal;

        if (internal < external) {
            throw new RuntimeException("Invalid referees distribution");
        }
    }
}