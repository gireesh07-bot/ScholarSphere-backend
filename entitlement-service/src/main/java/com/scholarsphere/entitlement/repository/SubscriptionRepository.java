package com.scholarsphere.entitlement.repository;

import com.scholarsphere.entitlement.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findByUserId(Long userId);

    Optional<Subscription> findFirstByUserIdAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Long userId,
            String status,
            LocalDate currentDateForStart,
            LocalDate currentDateForEnd
    );
}