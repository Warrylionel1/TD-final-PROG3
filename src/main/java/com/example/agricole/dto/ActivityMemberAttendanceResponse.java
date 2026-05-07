package com.example.agricole.dto;

import com.example.agricole.enums.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActivityMemberAttendanceResponse {

    private String id;
    private String memberIdentifier;
    private AttendanceStatus attendanceStatus;
}