package com.example.agricole.service;

import com.example.agricole.dto.CreateCollectivityActivity;
import com.example.agricole.dto.CollectivityActivityResponse;
import com.example.agricole.dto.MonthlyRecurrenceRule;
import com.example.agricole.entity.Activity;
import com.example.agricole.enums.MemberOccupation;
import com.example.agricole.exception.BadRequestException;
import com.example.agricole.exception.CollectivityNotFoundException;
import com.example.agricole.exception.InvalidEnumValueException;
import com.example.agricole.repository.ActivityConcernedRepository;
import com.example.agricole.repository.ActivityRepository;
import com.example.agricole.repository.CollectivityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final ActivityConcernedRepository activityConcernedRepository;
    private final CollectivityRepository collectivityRepository;

    public ActivityService(
            ActivityRepository activityRepository,
            ActivityConcernedRepository activityConcernedRepository,
            CollectivityRepository collectivityRepository
    ) {
        this.activityRepository = activityRepository;
        this.activityConcernedRepository = activityConcernedRepository;
        this.collectivityRepository = collectivityRepository;
    }

    public List<CollectivityActivityResponse> createActivities(
            String collectivityId,
            List<CreateCollectivityActivity> requests
    ) {

        if (requests == null || requests.isEmpty()) {
            throw new BadRequestException("Activities list cannot be empty");
        }

        if (!collectivityRepository.existsById(collectivityId)) {
            throw new CollectivityNotFoundException("Collectivity not found");
        }

        List<Activity> activities = requests.stream().map(req -> {

            if (req.getExecutiveDate() != null && req.getRecurrenceRule() != null) {
                throw new BadRequestException("Cannot provide both executiveDate and recurrenceRule");
            }

            if (req.getExecutiveDate() == null && req.getRecurrenceRule() == null) {
                throw new BadRequestException("Either executiveDate or recurrenceRule is required");
            }

            if (req.getLabel() == null || req.getLabel().isBlank()) {
                throw new BadRequestException("Label cannot be empty");
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
                            (a.getWeekOrdinal() == null && a.getDayOfWeek() == null)
                                    ? null
                                    : new MonthlyRecurrenceRule(
                                    a.getWeekOrdinal(),
                                    a.getDayOfWeek()
                            )
                    );
                })
                .toList();
    }

    public List<CollectivityActivityResponse> getActivitiesByCollectivity(String collectivityId) {

        if (!collectivityRepository.existsById(collectivityId)) {
            throw new CollectivityNotFoundException("Collectivity not found");
        }

        List<Activity> activities =
                activityRepository.findByCollectivityId(collectivityId);

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
                            (a.getWeekOrdinal() == null && a.getDayOfWeek() == null)
                                    ? null
                                    : new MonthlyRecurrenceRule(
                                    a.getWeekOrdinal(),
                                    a.getDayOfWeek()
                            )
                    );
                })
                .toList();
    }
}