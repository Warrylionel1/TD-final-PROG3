package com.example.agricole.controller;

import com.example.agricole.dto.CreateCollectivity;
import com.example.agricole.exception.BadRequestException;
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
}