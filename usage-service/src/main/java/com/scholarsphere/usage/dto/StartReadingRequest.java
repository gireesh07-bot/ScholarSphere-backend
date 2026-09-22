package com.scholarsphere.usage.dto;

import jakarta.validation.constraints.NotNull;

public class StartReadingRequest {

    private Long userId;

    @NotNull
    private Long contentId;


    public StartReadingRequest() {
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
}