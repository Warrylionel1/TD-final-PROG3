package com.example.agricole.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyRecurrenceRule {
    private Integer weekOrdinal;
    private String dayOfWeek; // MO, TU, WE...
}