package com.scholarsphere.usage.dto;

public class DashboardResponse {

    private Long userId;

    private long totalReadingSessions;

    private long completedContent;

    private long inProgressContent;

    private long totalDurationSeconds;

    private long totalEngagementEvents;

    public DashboardResponse() {
    }

    public DashboardResponse(
            Long userId,
            long totalReadingSessions,
            long completedContent,
            long inProgressContent,
            long totalDurationSeconds,
            long totalEngagementEvents) {

        this.userId = userId;
        this.totalReadingSessions = totalReadingSessions;
        this.completedContent = completedContent;
        this.inProgressContent = inProgressContent;
        this.totalDurationSeconds = totalDurationSeconds;
        this.totalEngagementEvents = totalEngagementEvents;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public long getTotalReadingSessions() {
        return totalReadingSessions;
    }

    public void setTotalReadingSessions(
            long totalReadingSessions) {

        this.totalReadingSessions = totalReadingSessions;
    }

    public long getCompletedContent() {
        return completedContent;
    }

    public void setCompletedContent(long completedContent) {
        this.completedContent = completedContent;
    }

    public long getInProgressContent() {
        return inProgressContent;
    }

    public void setInProgressContent(long inProgressContent) {
        this.inProgressContent = inProgressContent;
    }

    public long getTotalDurationSeconds() {
        return totalDurationSeconds;
    }

    public void setTotalDurationSeconds(
            long totalDurationSeconds) {

        this.totalDurationSeconds = totalDurationSeconds;
    }

    public long getTotalEngagementEvents() {
        return totalEngagementEvents;
    }

    public void setTotalEngagementEvents(
            long totalEngagementEvents) {

        this.totalEngagementEvents = totalEngagementEvents;
    }
}