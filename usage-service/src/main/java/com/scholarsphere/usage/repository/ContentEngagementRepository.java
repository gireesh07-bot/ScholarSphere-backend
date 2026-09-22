package com.scholarsphere.usage.repository;

import com.scholarsphere.usage.entity.ContentEngagement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentEngagementRepository
        extends JpaRepository<ContentEngagement, Long> {

    List<ContentEngagement>
    findByUserIdOrderByEventTimeDesc(Long userId);

    List<ContentEngagement>
    findByContentIdOrderByEventTimeDesc(Long contentId);

    long countByUserId(Long userId);
}