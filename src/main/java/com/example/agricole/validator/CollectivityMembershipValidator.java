package com.example.agricole.validator;

import com.example.agricole.entity.Collectivity;
import com.example.agricole.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class CollectivityMembershipValidator {

    public boolean isMemberInCollectivity(Member member, Collectivity collectivity) {

        if (collectivity == null || collectivity.getMembers() == null) {
            return false;
        }

        return collectivity.getMembers()
                .stream()
                .anyMatch(m -> m.getId().equals(member.getId()));
    }
}