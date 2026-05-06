package com.example.agricole.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectivityLocalStatistics {
    private MemberDescription memberDescription;
    private double earnedAmount;
    private double unpaidAmount;
}