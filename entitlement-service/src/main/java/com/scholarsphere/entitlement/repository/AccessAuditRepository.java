package com.scholarsphere.entitlement.repository;

import com.scholarsphere.entitlement.entity.AccessAudit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccessAuditRepository extends JpaRepository<AccessAudit, Long> {

    List<AccessAudit> findByContentIdOrderByTimestampDesc(Long contentId);

    List<AccessAudit> findByUserIdOrderByTimestampDesc(Long userId);
}