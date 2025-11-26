package org.example.stamppaw_backend.walk.repository;

import org.example.stamppaw_backend.walk.entity.Walk;
import org.example.stamppaw_backend.walk.entity.WalkStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

public interface WalkRepository extends JpaRepository<Walk, Long> {
    Optional<Walk> findByIdAndUserId(Long id, Long userId);

    boolean existsByUserId(Long userId);

    List<Walk> findAllByUser_Id(Long userId);

    @Query("""
             SELECT w FROM Walk w
             WHERE w.user.id = :userId
               AND (:keyword IS NULL OR w.memo LIKE %:keyword%)
             ORDER BY w.startTime DESC
            """)
    Page<Walk> searchByKeyword(@Param("userId") Long userId, @Param("keyword") String keyword, Pageable pageable);

    Page<Walk> findAllByUserIdAndStatusOrderByStartTimeDesc(Long userId, WalkStatus status, Pageable pageable);

}
