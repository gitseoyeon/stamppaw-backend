package org.example.stamppaw_backend.badge.repository;

import org.example.stamppaw_backend.badge.entity.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {

    Optional<UserBadge> findByUserIdAndBadgeId(Long userId, Long badgeId);

    List<UserBadge> findAllByUserId(Long userId);

    Optional<UserBadge> findByUserIdAndRepresentativeTrue(Long userId);
}
