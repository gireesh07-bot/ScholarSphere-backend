package com.scholarsphere.entitlement.controller;

import com.scholarsphere.entitlement.entity.SubscriptionPlan;
import com.scholarsphere.entitlement.service.SubscriptionPlanService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plans")
public class SubscriptionPlanController {

    private final SubscriptionPlanService subscriptionPlanService;

    public SubscriptionPlanController(
            SubscriptionPlanService subscriptionPlanService) {

        this.subscriptionPlanService = subscriptionPlanService;
    }

    @PostMapping
    public ResponseEntity<SubscriptionPlan> createPlan(
            @Valid @RequestBody SubscriptionPlan plan) {

        SubscriptionPlan createdPlan =
                subscriptionPlanService.createPlan(plan);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdPlan);
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionPlan>> getAllPlans() {

        return ResponseEntity.ok(
                subscriptionPlanService.getAllPlans()
        );
    }

    @GetMapping("/{planId}")
    public ResponseEntity<SubscriptionPlan> getPlanById(
            @PathVariable Long planId) {

        return ResponseEntity.ok(
                subscriptionPlanService.getPlanById(planId)
        );
    }
}