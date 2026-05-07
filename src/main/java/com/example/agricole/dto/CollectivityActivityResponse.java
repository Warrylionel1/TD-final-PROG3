package com.example.agricole.dto;

import com.example.agricole.enums.ActivityType;
import com.example.agricole.enums.MemberOccupation;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class CollectivityActivityResponse {

    private String id;
    private String label;
    private ActivityType activityType;
    private List<MemberOccupation> memberOccupationConcerned;
    private LocalDate executiveDate;
    private MonthlyRecurrenceRule recurrenceRule;
}