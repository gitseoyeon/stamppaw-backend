package org.example.stamppaw_backend.companion.repository;

import org.example.stamppaw_backend.companion.entity.CompanionReview;
import org.example.stamppaw_backend.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompanionReviewRepository extends JpaRepository<CompanionReview, Long> {
    @Query("SELECT r FROM CompanionReview r WHERE r.apply.companion.user = :user")
    Page<CompanionReview> findCompanionReviewByUser(Pageable pageable, @Param("user") User user);

    @Query("SELECT r FROM CompanionReview r WHERE r.apply.applicant = :user")
    Page<CompanionReview> findCompanionReviewByApplyUser(Pageable pageable, @Param("user") User user);

    boolean existsByApply_Id(Long applyId);
}
