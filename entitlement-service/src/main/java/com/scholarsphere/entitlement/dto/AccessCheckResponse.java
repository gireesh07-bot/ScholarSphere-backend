package com.scholarsphere.entitlement.dto;

public class AccessCheckResponse {

    private Long userId;

    private Long contentId;

    private boolean allowed;

    private String message;

    private String accessType;

    private Long entitlementId;

    public AccessCheckResponse() {
    }

    public AccessCheckResponse(
            Long userId,
            Long contentId,
            boolean allowed,
            String message,
            String accessType,
            Long entitlementId) {

        this.userId = userId;
        this.contentId = contentId;
        this.allowed = allowed;
        this.message = message;
        this.accessType = accessType;
        this.entitlementId = entitlementId;
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

    public boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public Long getEntitlementId() {
        return entitlementId;
    }

    public void setEntitlementId(Long entitlementId) {
        this.entitlementId = entitlementId;
    }
}