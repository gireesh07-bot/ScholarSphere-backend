package com.scholarsphere.usage.repository;

import com.scholarsphere.usage.entity.ReadingHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReadingHistoryRepository
        extends JpaRepository<ReadingHistory, Long> {

    List<ReadingHistory>
    findByUserIdOrderByLastAccessedAtDesc(Long userId);

    List<ReadingHistory>
    findByContentIdOrderByLastAccessedAtDesc(Long contentId);

    List<ReadingHistory>
    findByUserIdAndContentIdOrderByLastAccessedAtDesc(
            Long userId,
            Long contentId
    );

    long countByUserId(Long userId);

    long countByUserIdAndCompleted(
            Long userId,
            Boolean completed
    );
}