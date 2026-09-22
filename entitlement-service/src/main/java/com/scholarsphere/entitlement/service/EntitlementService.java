package com.scholarsphere.entitlement.service;

import com.scholarsphere.entitlement.dto.AccessCheckResponse;
import com.scholarsphere.entitlement.dto.EntitlementRequest;
import com.scholarsphere.entitlement.entity.AccessAudit;
import com.scholarsphere.entitlement.entity.Entitlement;
import com.scholarsphere.entitlement.entity.Subscription;
import com.scholarsphere.entitlement.repository.AccessAuditRepository;
import com.scholarsphere.entitlement.repository.EntitlementRepository;
import com.scholarsphere.entitlement.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EntitlementService {

    private final EntitlementRepository entitlementRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final AccessAuditRepository accessAuditRepository;

    public EntitlementService(
            EntitlementRepository entitlementRepository,
            SubscriptionRepository subscriptionRepository,
            AccessAuditRepository accessAuditRepository) {

        this.entitlementRepository = entitlementRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.accessAuditRepository = accessAuditRepository;
    }

    public Entitlement createEntitlement(
            EntitlementRequest request) {

        LocalDate today = LocalDate.now();

        Subscription subscription =
                subscriptionRepository
                        .findFirstByUserIdAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                                request.getUserId(),
                                "ACTIVE",
                                today,
                                today
                        )
                        .orElseThrow(() -> {

                            saveAudit(
                                    request.getUserId(),
                                    request.getContentId(),
                                    "ENTITLEMENT_CREATE",
                                    "DENIED",
                                    null
                            );

                            return new RuntimeException(
                                    "User does not have an active subscription"
                            );
                        });

        Entitlement existingEntitlement =
                entitlementRepository
                        .findByUserIdAndContentIdAndStatus(
                                request.getUserId(),
                                request.getContentId(),
                                "ACTIVE"
                        )
                        .orElse(null);

        if (existingEntitlement != null) {
            return existingEntitlement;
        }

        String accessType = request.getAccessType();

        if (accessType == null || accessType.isBlank()) {
            accessType = "READ";
        }

        Entitlement entitlement = new Entitlement();

        entitlement.setUserId(request.getUserId());
        entitlement.setContentId(request.getContentId());
        entitlement.setSubscription(subscription);
        entitlement.setAccessType(accessType);
        entitlement.setGrantedAt(LocalDateTime.now());

        entitlement.setExpiresAt(
                subscription.getEndDate()
                        .atTime(23, 59, 59)
        );

        entitlement.setStatus("ACTIVE");

        Entitlement savedEntitlement =
                entitlementRepository.save(entitlement);

        saveAudit(
                request.getUserId(),
                request.getContentId(),
                "ENTITLEMENT_CREATE",
                "ALLOWED",
                null
        );

        return savedEntitlement;
    }

    public AccessCheckResponse checkAccess(
            Long userId,
            Long contentId) {

        LocalDate today = LocalDate.now();

        Entitlement entitlement =
                entitlementRepository
                        .findByUserIdAndContentIdAndStatus(
                                userId,
                                contentId,
                                "ACTIVE"
                        )
                        .orElse(null);

        if (entitlement != null) {

            if (entitlement.getExpiresAt() != null
                    && entitlement.getExpiresAt()
                    .isBefore(LocalDateTime.now())) {

                entitlement.setStatus("EXPIRED");

                entitlementRepository.save(entitlement);

                saveAudit(
                        userId,
                        contentId,
                        "CONTENT_ACCESS",
                        "DENIED",
                        null
                );

                return new AccessCheckResponse(
                        userId,
                        contentId,
                        false,
                        "Entitlement has expired",
                        null,
                        entitlement.getEntitlementId()
                );
            }

            saveAudit(
                    userId,
                    contentId,
                    "CONTENT_ACCESS",
                    "ALLOWED",
                    null
            );

            return new AccessCheckResponse(
                    userId,
                    contentId,
                    true,
                    "Access allowed",
                    entitlement.getAccessType(),
                    entitlement.getEntitlementId()
            );
        }

        boolean hasActiveSubscription =
                subscriptionRepository
                        .findFirstByUserIdAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                                userId,
                                "ACTIVE",
                                today,
                                today
                        )
                        .isPresent();

        if (!hasActiveSubscription) {

            saveAudit(
                    userId,
                    contentId,
                    "CONTENT_ACCESS",
                    "DENIED",
                    null
            );

            return new AccessCheckResponse(
                    userId,
                    contentId,
                    false,
                    "User does not have an active subscription",
                    null,
                    null
            );
        }

        saveAudit(
                userId,
                contentId,
                "CONTENT_ACCESS",
                "DENIED",
                null
        );

        return new AccessCheckResponse(
                userId,
                contentId,
                false,
                "No entitlement exists for this content",
                null,
                null
        );
    }

    public List<Entitlement> getEntitlementsByUserId(
            Long userId) {

        return entitlementRepository.findByUserId(userId);
    }

    public void revokeEntitlement(
            Long entitlementId) {

        Entitlement entitlement =
                entitlementRepository.findById(entitlementId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Entitlement not found with id: "
                                                + entitlementId
                                )
                        );

        entitlement.setStatus("REVOKED");

        entitlementRepository.save(entitlement);

        saveAudit(
                entitlement.getUserId(),
                entitlement.getContentId(),
                "ENTITLEMENT_REVOKE",
                "ALLOWED",
                null
        );
    }

    public List<AccessAudit> getAuditByContentId(
            Long contentId) {

        return accessAuditRepository
                .findByContentIdOrderByTimestampDesc(contentId);
    }

    private void saveAudit(
            Long userId,
            Long contentId,
            String action,
            String result,
            String ipAddress) {

        AccessAudit audit = new AccessAudit();

        audit.setUserId(userId);
        audit.setContentId(contentId);
        audit.setAction(action);
        audit.setResult(result);
        audit.setTimestamp(LocalDateTime.now());
        audit.setIpAddress(ipAddress);

        accessAuditRepository.save(audit);
    }
}