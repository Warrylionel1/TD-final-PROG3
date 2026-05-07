package com.example.agricole.dto;

import com.example.agricole.enums.AttendanceStatus;
import lombok.Data;

@Data
public class CreateActivityMemberAttendance {

    private String memberIdentifier;
    private AttendanceStatus attendanceStatus;
}