package com.scholarsphere.entitlement.controller;

import com.scholarsphere.entitlement.dto.AccessCheckResponse;
import com.scholarsphere.entitlement.dto.EntitlementRequest;
import com.scholarsphere.entitlement.entity.AccessAudit;
import com.scholarsphere.entitlement.entity.Entitlement;
import com.scholarsphere.entitlement.service.EntitlementService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/entitlements")
public class EntitlementController {

    private final EntitlementService entitlementService;

    public EntitlementController(
            EntitlementService entitlementService) {

        this.entitlementService = entitlementService;
    }

    @PostMapping
    public ResponseEntity<Entitlement> createEntitlement(
            @Valid @RequestBody EntitlementRequest request) {

        Entitlement entitlement =
                entitlementService.createEntitlement(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(entitlement);
    }

    @GetMapping("/check/{contentId}")
    public ResponseEntity<AccessCheckResponse> checkAccess(
            @PathVariable Long contentId,
            @RequestParam Long userId) {

        AccessCheckResponse response =
                entitlementService.checkAccess(
                        userId,
                        contentId
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Entitlement>>
    getEntitlementsByUserId(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                entitlementService
                        .getEntitlementsByUserId(userId)
        );
    }

    @DeleteMapping("/{entitlementId}")
    public ResponseEntity<String> revokeEntitlement(
            @PathVariable Long entitlementId) {

        entitlementService.revokeEntitlement(
                entitlementId
        );

        return ResponseEntity.ok(
                "Entitlement revoked successfully"
        );
    }

    @GetMapping("/audit/{contentId}")
    public ResponseEntity<List<AccessAudit>>
    getAuditByContentId(
            @PathVariable Long contentId) {

        return ResponseEntity.ok(
                entitlementService
                        .getAuditByContentId(contentId)
        );
    }
}