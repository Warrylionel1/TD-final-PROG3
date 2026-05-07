package com.example.agricole.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class MembershipFeeRaw {

    private final String id;
    private final double amount;
    private final String frequency;
    private final LocalDate eligibleFrom;
}
