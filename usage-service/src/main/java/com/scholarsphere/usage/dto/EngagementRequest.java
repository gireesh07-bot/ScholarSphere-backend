package com.scholarsphere.usage.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EngagementRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Content ID is required")
    private Long contentId;

    @NotBlank(message = "Event type is required")
    private String eventType;

    public EngagementRequest() {
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

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}