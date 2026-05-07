package com.example.agricole.dto;



import com.example.agricole.enums.ActivityType;
import com.example.agricole.enums.MemberOccupation;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CreateCollectivityActivity {

    private String label;

    private ActivityType activityType;

    private List<MemberOccupation> memberOccupationConcerned;

    private MonthlyRecurrenceRule recurrenceRule;

    private LocalDate executiveDate;
}