package com.example.agricole.validator;

import com.example.agricole.entity.Member;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class MemberExperienceValidator {

    public boolean has6MonthsExperience(Member member) {
        if (member == null || member.getJoinDate() == null) {
            return false;
        }

        return member.getJoinDate()
                .isBefore(LocalDate.now().minusMonths(6));
    }
}
