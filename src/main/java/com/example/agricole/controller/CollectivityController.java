package com.example.agricole.controller;

import com.example.agricole.dto.*;
import com.example.agricole.entity.Collectivity;
import com.example.agricole.entity.CollectivityTransaction;
import com.example.agricole.entity.MembershipFee;
import com.example.agricole.exception.BadRequestException;
import com.example.agricole.exception.CollectivityNotFoundException;
import com.example.agricole.exception.ConflictException;
import com.example.agricole.exception.MemberNotFoundException;
import com.example.agricole.service.ActivityService;
import com.example.agricole.service.CollectivityService;
import com.example.agricole.service.StatisticsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/collectivities")
public class CollectivityController {

    private final CollectivityService collectivityService;
    private final StatisticsService statisticsService;
    private final ActivityService activityService;

    public CollectivityController(CollectivityService collectivityService,
                                  StatisticsService statisticsService,
                                  ActivityService activityService) {
        this.collectivityService = collectivityService;
        this.statisticsService = statisticsService;
        this.activityService = activityService;
    }

    @PostMapping
    public ResponseEntity<?> createCollectivities(
            @RequestBody List<CreateCollectivity> requests) {

        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(collectivityService.createCollectivities(requests));

        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());

        } catch (MemberNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/{collectivityId}/assign-identity")
    public ResponseEntity<?> assignIdentity(
            @PathVariable String collectivityId,
            @RequestBody AssignIdentityRequest request) {

        try {
            if (request.getNumber() == null || request.getNumber().isBlank() ||
                    request.getName() == null || request.getName().isBlank()) {
                return ResponseEntity.badRequest().body("Number and name are required");
            }
            Collectivity updated = collectivityService.assignIdentity(collectivityId, request);
            return ResponseEntity.ok(updated);
        } catch (CollectivityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}/membershipFees")
    public ResponseEntity<List<MembershipFee>> getMembershipFees(@PathVariable String id) {
        return ResponseEntity.ok(collectivityService.getMembershipFees(id));
    }

    @PostMapping("/{id}/membershipFees")
    public ResponseEntity<List<MembershipFee>> createMembershipFees(
            @PathVariable String id,
            @RequestBody List<CreateMembershipFee> fees) {
        return ResponseEntity.ok(collectivityService.createMembershipFees(id, fees));
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<CollectivityTransaction>> getTransactions(
            @PathVariable String id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(collectivityService.getTransactions(id, from, to));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        try {
            Collectivity result = collectivityService.getCollectivityById(id);
            return ResponseEntity.ok(result);
        }catch (CollectivityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/{id}/financialAccounts")
    public ResponseEntity<?> getFinancialAccounts(
            @PathVariable String id,
            @RequestParam("at") LocalDate at
    ) {
        try {
            return ResponseEntity.ok(
                    collectivityService.getFinancialAccounts(id, at)
            );
        } catch (CollectivityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error");
        }
    }

    @GetMapping("/{id}/statistics")
    public ResponseEntity<?> getLocalStatistics(
            @PathVariable String id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        try {
            return ResponseEntity.ok(
                    statisticsService.getLocalStatistics(id, from, to)
            );

        } catch (CollectivityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());

        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/statistics")
    public ResponseEntity<?> getOverallStatistics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        try {
            return ResponseEntity.ok(
                    statisticsService.getOverallStatistics(from, to)
            );

        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/{id}/activities")
    public ResponseEntity<?> createActivities(
            @PathVariable String id,
            @RequestBody List<CreateCollectivityActivity> requests
    ) {
        try {

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(activityService.createActivities(id, requests));

        } catch (CollectivityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());

        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error");
        }
    }
}