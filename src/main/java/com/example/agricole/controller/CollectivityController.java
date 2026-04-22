package com.example.agricole.controller;

import com.example.agricole.dto.AssignIdentityRequest;
import com.example.agricole.dto.CreateCollectivity;
import com.example.agricole.entity.Collectivity;
import com.example.agricole.exception.BadRequestException;
import com.example.agricole.exception.CollectivityNotFoundException;
import com.example.agricole.exception.ConflictException;
import com.example.agricole.exception.MemberNotFoundException;
import com.example.agricole.service.CollectivityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collectivities")
public class CollectivityController {

    private final CollectivityService collectivityService;

    public CollectivityController(CollectivityService collectivityService) {
        this.collectivityService = collectivityService;
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
}