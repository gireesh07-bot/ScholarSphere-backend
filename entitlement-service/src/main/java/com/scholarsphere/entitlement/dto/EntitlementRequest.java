package com.scholarsphere.entitlement.dto;

import jakarta.validation.constraints.NotNull;

public class EntitlementRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Content ID is required")
    private Long contentId;

    private String accessType;

    public EntitlementRequest() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }
}