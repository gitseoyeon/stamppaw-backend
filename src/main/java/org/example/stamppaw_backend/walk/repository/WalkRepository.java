package org.example.stamppaw_backend.walk.repository;

import org.example.stamppaw_backend.user.entity.User;
import org.example.stamppaw_backend.walk.entity.Walk;
import org.example.stamppaw_backend.walk.entity.WalkStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

public interface WalkRepository extends JpaRepository<Walk, Long> {
    Optional<Walk> findByIdAndUserId(Long id, Long userId);

    Page<Walk> findAllByUserIdAndStatusOrderByStartTimeDesc(Long userId, WalkStatus status, Pageable pageable);

}
