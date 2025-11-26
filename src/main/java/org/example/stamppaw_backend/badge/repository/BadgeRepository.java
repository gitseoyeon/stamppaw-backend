package org.example.stamppaw_backend.badge.repository;

import org.example.stamppaw_backend.badge.entity.Badge;
import org.example.stamppaw_backend.badge.entity.BadgeCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface BadgeRepository extends JpaRepository<Badge, Long> {
    List<Badge> findAllByOrderByIdAsc();
    List<Badge> findAllByActiveTrue();
    boolean existsByBadgeCode(BadgeCode badgeCode);
}
