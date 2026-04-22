package com.example.agricole.validator;

import com.example.agricole.entity.Collectivity;
import com.example.agricole.entity.Member;
import com.example.agricole.exception.BadRequestException;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class MemberRefereeValidator {

    private final CollectivityMembershipValidator membershipValidator;

    public MemberRefereeValidator(CollectivityMembershipValidator membershipValidator) {
        this.membershipValidator = membershipValidator;
    }

    public void validate(List<Member> referees, Collectivity collectivity) {

        if (referees == null || referees.size() < 2) {
            throw new BadRequestException("At least 2 referees required");
        }

        long internal = referees.stream()
                .filter(r -> membershipValidator.isMemberInCollectivity(r, collectivity))
                .count();

        long external = referees.size() - internal;

        if (internal < external) {
            throw new BadRequestException("Invalid referees distribution");
        }
    }
}