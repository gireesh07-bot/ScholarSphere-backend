package com.scholarsphere.entitlement.repository;

import com.scholarsphere.entitlement.entity.Entitlement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EntitlementRepository extends JpaRepository<Entitlement, Long> {

    List<Entitlement> findByUserId(Long userId);

    List<Entitlement> findByContentId(Long contentId);

    Optional<Entitlement> findByUserIdAndContentIdAndStatus(
            Long userId,
            Long contentId,
            String status
    );
}