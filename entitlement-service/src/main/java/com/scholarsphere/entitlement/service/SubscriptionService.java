package com.scholarsphere.entitlement.service;

import com.scholarsphere.entitlement.dto.SubscriptionRequest;
import com.scholarsphere.entitlement.entity.Subscription;
import com.scholarsphere.entitlement.entity.SubscriptionPlan;
import com.scholarsphere.entitlement.repository.SubscriptionRepository;
import com.scholarsphere.entitlement.repository.SubscriptionPlanRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;

    public SubscriptionService(
            SubscriptionRepository subscriptionRepository,
            SubscriptionPlanRepository subscriptionPlanRepository) {

        this.subscriptionRepository = subscriptionRepository;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
    }

    public Subscription createSubscription(
            SubscriptionRequest request) {

        SubscriptionPlan plan =
                subscriptionPlanRepository.findById(request.getPlanId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Subscription plan not found with id: "
                                                + request.getPlanId()
                                )
                        );

        if (!"ACTIVE".equalsIgnoreCase(plan.getStatus())) {
            throw new RuntimeException(
                    "Subscription plan is not active"
            );
        }

        LocalDate startDate = LocalDate.now();

        LocalDate endDate =
                startDate.plusDays(plan.getDurationDays());

        Subscription subscription = new Subscription();

        subscription.setUserId(request.getUserId());
        subscription.setPlan(plan);
        subscription.setStartDate(startDate);
        subscription.setEndDate(endDate);
        subscription.setStatus("ACTIVE");

        return subscriptionRepository.save(subscription);
    }

    public List<Subscription> getSubscriptionsByUserId(
            Long userId) {

        return subscriptionRepository.findByUserId(userId);
    }

    public Subscription getSubscriptionById(
            Long subscriptionId) {

        return subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Subscription not found with id: "
                                        + subscriptionId
                        )
                );
    }
}