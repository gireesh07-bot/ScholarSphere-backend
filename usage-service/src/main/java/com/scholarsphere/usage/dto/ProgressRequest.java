package com.scholarsphere.usage.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

public class ProgressRequest {

    @DecimalMin(
            value = "0.0",
            message = "Progress cannot be less than 0"
    )
    @DecimalMax(
            value = "100.0",
            message = "Progress cannot exceed 100"
    )
    private Double progressPercentage;

    private Long durationSeconds;

    public ProgressRequest() {
    }

    public Double getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(
            Double progressPercentage) {

        this.progressPercentage = progressPercentage;
    }

    public Long getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Long durationSeconds) {
        this.durationSeconds = durationSeconds;
    }
}