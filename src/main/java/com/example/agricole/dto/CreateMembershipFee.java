package com.example.agricole.dto;

import com.example.agricole.enums.Frequency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMembershipFee {
    private LocalDate eligibleFrom;
    private Frequency frequency;
    private double amount;
    private String label;
}