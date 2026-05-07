package com.example.agricole.service;

import com.example.agricole.dto.ActivityMemberAttendanceResponse;
import com.example.agricole.dto.CreateActivityMemberAttendance;
import com.example.agricole.enums.AttendanceStatus;
import com.example.agricole.exception.ActivityNotFoundException;
import com.example.agricole.exception.AttendanceAlreadySetException;
import com.example.agricole.exception.BadRequestException;
import com.example.agricole.exception.MemberNotFoundException;
import com.example.agricole.repository.ActivityAttendanceRepository;
import com.example.agricole.repository.ActivityRepository;
import com.example.agricole.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityAttendanceService {

    private final ActivityAttendanceRepository repository;
    private final ActivityRepository activityRepository;
    private final MemberRepository memberRepository;

    public ActivityAttendanceService(
            ActivityAttendanceRepository repository,
            ActivityRepository activityRepository,
            MemberRepository memberRepository
    ) {
        this.repository = repository;
        this.activityRepository = activityRepository;
        this.memberRepository = memberRepository;
    }

    public List<ActivityMemberAttendanceResponse> markAttendance(
            String activityId,
            List<CreateActivityMemberAttendance> requests
    ) {

        if (requests == null || requests.isEmpty()) {
            throw new BadRequestException("Attendance list cannot be empty");
        }

        if (!activityRepository.existsById(activityId)) {
            throw new ActivityNotFoundException("Activity not found");
        }

        for (CreateActivityMemberAttendance req : requests) {

            if (req.getMemberIdentifier() == null || req.getMemberIdentifier().isBlank()) {
                throw new BadRequestException("Member identifier is required");
            }

            if (!memberRepository.existsById(req.getMemberIdentifier())) {
                throw new MemberNotFoundException("Member not found: " + req.getMemberIdentifier());
            }

            List<AttendanceStatus> existing =
                    repository.findByActivityIdAndMember(activityId, req.getMemberIdentifier());

            if (!existing.isEmpty()) {
                throw new AttendanceAlreadySetException(
                        "Attendance already set for member: " + req.getMemberIdentifier()
                );
            }
        }

        repository.saveAll(activityId, requests);

        return requests.stream()
                .map(req -> new ActivityMemberAttendanceResponse(
                        null,
                        req.getMemberIdentifier(),
                        req.getAttendanceStatus()
                ))
                .toList();
    }
}