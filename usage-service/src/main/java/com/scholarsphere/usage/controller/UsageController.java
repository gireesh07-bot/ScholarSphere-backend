package com.scholarsphere.usage.controller;

import com.scholarsphere.usage.dto.DashboardResponse;
import com.scholarsphere.usage.dto.EngagementRequest;
import com.scholarsphere.usage.dto.ProgressRequest;
import com.scholarsphere.usage.dto.StartReadingRequest;

import com.scholarsphere.usage.entity.ContentEngagement;
import com.scholarsphere.usage.entity.ReadingHistory;

import com.scholarsphere.usage.service.UsageService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usage")
public class UsageController {

    private final UsageService usageService;


    public UsageController(
            UsageService usageService) {

        this.usageService =
                usageService;
    }


    // =========================================================
    // START READING
    // =========================================================

    @PostMapping("/start")
    public ResponseEntity<ReadingHistory> startReading(
            @Valid @RequestBody StartReadingRequest request,
            Authentication authentication) {

        Long userId =
                getAuthenticatedUserId(
                        authentication
                );

        // Force the authenticated user's ID.
        request.setUserId(userId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        usageService.startReading(
                                request
                        )
                );
    }


    // =========================================================
    // UPDATE PROGRESS
    // =========================================================

    @PutMapping("/{historyId}/progress")
    public ResponseEntity<ReadingHistory> updateProgress(
            @PathVariable Long historyId,
            @Valid @RequestBody ProgressRequest request,
            Authentication authentication) {

        Long userId =
                getAuthenticatedUserId(
                        authentication
                );

        return ResponseEntity.ok(
                usageService.updateProgress(
                        historyId,
                        request,
                        userId
                )
        );
    }


    // =========================================================
    // COMPLETE READING
    // =========================================================

    @PostMapping("/{historyId}/complete")
    public ResponseEntity<ReadingHistory> completeReading(
            @PathVariable Long historyId,
            Authentication authentication) {

        Long userId =
                getAuthenticatedUserId(
                        authentication
                );

        return ResponseEntity.ok(
                usageService.completeReading(
                        historyId,
                        userId
                )
        );
    }


    // =========================================================
    // GET USER HISTORY
    // =========================================================

    @GetMapping("/history/{userId}")
    public ResponseEntity<List<ReadingHistory>>
    getUserHistory(
            @PathVariable Long userId,
            Authentication authentication) {

        Long authenticatedUserId =
                getAuthenticatedUserId(
                        authentication
                );

        // User can only access own history
        if (!userId.equals(authenticatedUserId)) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }

        return ResponseEntity.ok(
                usageService.getUserHistory(
                        authenticatedUserId
                )
        );
    }


    // =========================================================
    // GET CONTENT HISTORY
    // =========================================================

    @GetMapping("/content/{contentId}")
    public ResponseEntity<List<ReadingHistory>>
    getContentHistory(
            @PathVariable Long contentId) {

        return ResponseEntity.ok(
                usageService.getContentHistory(
                        contentId
                )
        );
    }


    // =========================================================
    // RECORD ENGAGEMENT
    // =========================================================

    @PostMapping("/engagement")
    public ResponseEntity<ContentEngagement>
    recordEngagement(
            @Valid @RequestBody EngagementRequest request,
            Authentication authentication) {

        Long userId =
                getAuthenticatedUserId(
                        authentication
                );

        // Force authenticated user's ID
        request.setUserId(userId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        usageService.recordEngagement(
                                request
                        )
                );
    }


    // =========================================================
    // GET USER ENGAGEMENT
    // =========================================================

    @GetMapping("/engagement/user/{userId}")
    public ResponseEntity<List<ContentEngagement>>
    getUserEngagement(
            @PathVariable Long userId,
            Authentication authentication) {

        Long authenticatedUserId =
                getAuthenticatedUserId(
                        authentication
                );

        // User can only see own engagement
        if (!userId.equals(authenticatedUserId)) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }

        return ResponseEntity.ok(
                usageService.getUserEngagement(
                        authenticatedUserId
                )
        );
    }


    // =========================================================
    // DASHBOARD
    // =========================================================

    @GetMapping("/dashboard/{userId}")
    public ResponseEntity<DashboardResponse>
    getDashboard(
            @PathVariable Long userId,
            Authentication authentication) {

        Long authenticatedUserId =
                getAuthenticatedUserId(
                        authentication
                );

        // User can only see own dashboard
        if (!userId.equals(authenticatedUserId)) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }

        return ResponseEntity.ok(
                usageService.getDashboard(
                        authenticatedUserId
                )
        );
    }


    // =========================================================
    // GET AUTHENTICATED USER ID
    // =========================================================

    private Long getAuthenticatedUserId(
            Authentication authentication) {

        return (Long) authentication.getDetails();
    }
}