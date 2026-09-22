package com.scholarsphere.entitlement.controller;

import com.scholarsphere.entitlement.dto.SubscriptionRequest;
import com.scholarsphere.entitlement.entity.Subscription;
import com.scholarsphere.entitlement.service.SubscriptionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(
            SubscriptionService subscriptionService) {

        this.subscriptionService = subscriptionService;
    }

    @PostMapping
    public ResponseEntity<Subscription> createSubscription(
            @Valid @RequestBody SubscriptionRequest request) {

        Subscription subscription =
                subscriptionService.createSubscription(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(subscription);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Subscription>>
    getSubscriptionsByUserId(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                subscriptionService
                        .getSubscriptionsByUserId(userId)
        );
    }

    @GetMapping("/{subscriptionId}")
    public ResponseEntity<Subscription>
    getSubscriptionById(
            @PathVariable Long subscriptionId) {

        return ResponseEntity.ok(
                subscriptionService
                        .getSubscriptionById(subscriptionId)
        );
    }
}