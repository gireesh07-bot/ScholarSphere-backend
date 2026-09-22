package com.scholarsphere.entitlement.service;

import com.scholarsphere.entitlement.entity.SubscriptionPlan;
import com.scholarsphere.entitlement.repository.SubscriptionPlanRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionPlanService {

    private final SubscriptionPlanRepository subscriptionPlanRepository;

    public SubscriptionPlanService(
            SubscriptionPlanRepository subscriptionPlanRepository) {

        this.subscriptionPlanRepository = subscriptionPlanRepository;
    }

    public SubscriptionPlan createPlan(SubscriptionPlan plan) {

        if (subscriptionPlanRepository
                .findByPlanName(plan.getPlanName())
                .isPresent()) {

            throw new RuntimeException(
                    "Subscription plan already exists: " + plan.getPlanName()
            );
        }

        if (plan.getStatus() == null || plan.getStatus().isBlank()) {
            plan.setStatus("ACTIVE");
        }

        return subscriptionPlanRepository.save(plan);
    }

    public List<SubscriptionPlan> getAllPlans() {

        return subscriptionPlanRepository.findAll();
    }

    public SubscriptionPlan getPlanById(Long planId) {

        return subscriptionPlanRepository.findById(planId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Subscription plan not found with id: " + planId
                        )
                );
    }
}