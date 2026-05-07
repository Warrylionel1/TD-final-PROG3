package com.example.agricole.entity;

import com.example.agricole.enums.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityMemberAttendance {

    private String id;
    private String activityId;
    private String memberId;
    private AttendanceStatus attendanceStatus;
}