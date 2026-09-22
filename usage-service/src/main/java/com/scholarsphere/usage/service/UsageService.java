package com.scholarsphere.usage.service;

import com.scholarsphere.usage.dto.DashboardResponse;
import com.scholarsphere.usage.dto.EngagementRequest;
import com.scholarsphere.usage.dto.ProgressRequest;
import com.scholarsphere.usage.dto.StartReadingRequest;

import com.scholarsphere.usage.entity.ContentEngagement;
import com.scholarsphere.usage.entity.ReadingHistory;

import com.scholarsphere.usage.repository.ContentEngagementRepository;
import com.scholarsphere.usage.repository.ReadingHistoryRepository;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UsageService {

    private final ReadingHistoryRepository readingHistoryRepository;

    private final ContentEngagementRepository contentEngagementRepository;

    public UsageService(
            ReadingHistoryRepository readingHistoryRepository,
            ContentEngagementRepository contentEngagementRepository) {

        this.readingHistoryRepository = readingHistoryRepository;
        this.contentEngagementRepository = contentEngagementRepository;
    }

    // =========================================================
    // START READING
    // =========================================================

    public ReadingHistory startReading(
            StartReadingRequest request) {

        ReadingHistory history = new ReadingHistory();

        LocalDateTime now = LocalDateTime.now();

        history.setUserId(request.getUserId());
        history.setContentId(request.getContentId());
        history.setStartedAt(now);
        history.setLastAccessedAt(now);
        history.setDurationSeconds(0L);
        history.setProgressPercentage(0.0);
        history.setCompleted(false);

        return readingHistoryRepository.save(history);
    }

    // =========================================================
    // UPDATE READING PROGRESS
    // =========================================================

    public ReadingHistory updateProgress(
            Long historyId,
            ProgressRequest request,
            Long authenticatedUserId) {

        ReadingHistory history =
                readingHistoryRepository.findById(historyId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Reading history not found with id: "
                                                + historyId
                                )
                        );

        // Check whether this history belongs to logged-in user
        verifyOwnership(
                history,
                authenticatedUserId
        );

        // Update progress
        if (request.getProgressPercentage() != null) {

            history.setProgressPercentage(
                    request.getProgressPercentage()
            );
        }

        // Update duration
        if (request.getDurationSeconds() != null) {

            history.setDurationSeconds(
                    request.getDurationSeconds()
            );
        }

        // Update last accessed time
        history.setLastAccessedAt(
                LocalDateTime.now()
        );

        return readingHistoryRepository.save(history);
    }

    // =========================================================
    // COMPLETE READING
    // =========================================================

    public ReadingHistory completeReading(
            Long historyId,
            Long authenticatedUserId) {

        ReadingHistory history =
                readingHistoryRepository.findById(historyId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Reading history not found with id: "
                                                + historyId
                                )
                        );

        // Check whether this history belongs to logged-in user
        verifyOwnership(
                history,
                authenticatedUserId
        );

        // Mark as completed
        history.setProgressPercentage(100.0);

        history.setCompleted(true);

        history.setLastAccessedAt(
                LocalDateTime.now()
        );

        return readingHistoryRepository.save(history);
    }

    // =========================================================
    // GET USER READING HISTORY
    // =========================================================

    public List<ReadingHistory> getUserHistory(
            Long userId) {

        return readingHistoryRepository
                .findByUserIdOrderByLastAccessedAtDesc(
                        userId
                );
    }

    // =========================================================
    // GET CONTENT HISTORY
    // =========================================================

    public List<ReadingHistory> getContentHistory(
            Long contentId) {

        return readingHistoryRepository
                .findByContentIdOrderByLastAccessedAtDesc(
                        contentId
                );
    }

    // =========================================================
    // RECORD ENGAGEMENT
    // =========================================================

    public ContentEngagement recordEngagement(
            EngagementRequest request) {

        ContentEngagement engagement =
                new ContentEngagement();

        engagement.setUserId(
                request.getUserId()
        );

        engagement.setContentId(
                request.getContentId()
        );

        engagement.setEventType(
                request.getEventType()
        );

        engagement.setEventTime(
                LocalDateTime.now()
        );

        return contentEngagementRepository.save(
                engagement
        );
    }

    // =========================================================
    // GET USER ENGAGEMENT
    // =========================================================

    public List<ContentEngagement> getUserEngagement(
            Long userId) {

        return contentEngagementRepository
                .findByUserIdOrderByEventTimeDesc(
                        userId
                );
    }

    // =========================================================
    // GET DASHBOARD
    // =========================================================

    public DashboardResponse getDashboard(
            Long userId) {

        List<ReadingHistory> history =
                readingHistoryRepository
                        .findByUserIdOrderByLastAccessedAtDesc(
                                userId
                        );

        // -----------------------------------------------------
        // TOTAL READING DURATION
        // -----------------------------------------------------

        long totalDuration =
                history.stream()
                        .mapToLong(
                                item ->
                                        item.getDurationSeconds() == null
                                                ? 0L
                                                : item.getDurationSeconds()
                        )
                        .sum();

        // -----------------------------------------------------
        // TOTAL SESSIONS
        // -----------------------------------------------------

        long totalSessions =
                history.size();

        // -----------------------------------------------------
        // COMPLETED
        // -----------------------------------------------------

        long completed =
                history.stream()
                        .filter(
                                item ->
                                        Boolean.TRUE.equals(
                                                item.getCompleted()
                                        )
                        )
                        .count();

        // -----------------------------------------------------
        // IN PROGRESS
        // -----------------------------------------------------

        long inProgress =
                history.stream()
                        .filter(
                                item ->
                                        !Boolean.TRUE.equals(
                                                item.getCompleted()
                                        )
                        )
                        .count();

        // -----------------------------------------------------
        // ENGAGEMENT EVENTS
        // -----------------------------------------------------

        long engagementEvents =
                contentEngagementRepository
                        .countByUserId(userId);

        // -----------------------------------------------------
        // CREATE RESPONSE
        // -----------------------------------------------------

        return new DashboardResponse(
                userId,
                totalSessions,
                completed,
                inProgress,
                totalDuration,
                engagementEvents
        );
    }

    // =========================================================
    // VERIFY HISTORY OWNERSHIP
    // =========================================================

    private void verifyOwnership(
            ReadingHistory history,
            Long authenticatedUserId) {

        if (!history.getUserId()
                .equals(authenticatedUserId)) {

            throw new AccessDeniedException(
                    "You are not allowed to modify this reading history"
            );
        }
    }
}