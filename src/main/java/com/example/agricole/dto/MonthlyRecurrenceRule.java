package com.example.agricole.dto;

import lombok.Data;

@Data
public class MonthlyRecurrenceRule {
    private Integer weekOrdinal;
    private String dayOfWeek; // MO, TU, WE...
}