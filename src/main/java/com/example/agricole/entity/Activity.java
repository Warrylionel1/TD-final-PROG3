package com.example.agricole.entity;

import com.example.agricole.enums.ActivityType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Activity {
    private String id;
    private String collectivityId;
    private String label;
    private ActivityType activityType;
    private LocalDate executiveDate;
    private Integer weekOrdinal;
    private String dayOfWeek;
}