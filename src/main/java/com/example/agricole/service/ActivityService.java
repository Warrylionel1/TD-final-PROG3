package com.example.agricole.service;

import com.example.agricole.dto.CreateCollectivityActivity;
import com.example.agricole.dto.CollectivityActivityResponse;
import com.example.agricole.entity.Activity;
import com.example.agricole.enums.MemberOccupation;
import com.example.agricole.enums.ActivityType;
import com.example.agricole.exception.BadRequestException;
import com.example.agricole.exception.InvalidEnumValueException;
import com.example.agricole.repository.ActivityConcernedRepository;
import com.example.agricole.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final ActivityConcernedRepository activityConcernedRepository;

    public List<CollectivityActivityResponse> createActivities(
            String collectivityId,
            List<CreateCollectivityActivity> requests
    ) {

        if (requests == null || requests.isEmpty()) {
            throw new BadRequestException("Activities list cannot be empty");
        }

        List<Activity> activities = requests.stream().map(req -> {

            if (req.getExecutiveDate() != null && req.getRecurrenceRule() != null) {
                throw new BadRequestException("Cannot provide both executiveDate and recurrenceRule");
            }

            if (req.getExecutiveDate() == null && req.getRecurrenceRule() == null) {
                throw new BadRequestException("Either executiveDate or recurrenceRule is required");
            }

            Activity a = new Activity();

            a.setId(UUID.randomUUID().toString());
            a.setCollectivityId(collectivityId);
            a.setLabel(req.getLabel());

            try {
                a.setActivityType(req.getActivityType());
            } catch (Exception e) {
                throw new InvalidEnumValueException("Invalid activityType value");
            }

            if (req.getExecutiveDate() != null) {
                a.setExecutiveDate(req.getExecutiveDate());
            }

            if (req.getRecurrenceRule() != null) {
                a.setWeekOrdinal(req.getRecurrenceRule().getWeekOrdinal());
                a.setDayOfWeek(req.getRecurrenceRule().getDayOfWeek());
            }

            return a;

        }).toList();

        activityRepository.saveAll(activities);

        for (int i = 0; i < activities.size(); i++) {
            Activity activity = activities.get(i);
            CreateCollectivityActivity req = requests.get(i);

            if (req.getMemberOccupationConcerned() != null
                    && !req.getMemberOccupationConcerned().isEmpty()) {

                activityConcernedRepository.saveAll(
                        activity.getId(),
                        req.getMemberOccupationConcerned()
                );
            }
        }

        return activities.stream()
                .map(a -> {
                    List<MemberOccupation> occupations =
                            activityConcernedRepository.findByActivityId(a.getId());

                    return new CollectivityActivityResponse(
                            a.getId(),
                            a.getLabel(),
                            a.getActivityType(),
                            occupations,
                            a.getExecutiveDate(),
                            a.getWeekOrdinal(),
                            a.getDayOfWeek()
                    );
                })
                .toList();
    }
}