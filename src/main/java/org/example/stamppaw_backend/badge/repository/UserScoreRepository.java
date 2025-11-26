package org.example.stamppaw_backend.badge.repository;

import org.example.stamppaw_backend.user.entity.User;
import org.example.stamppaw_backend.badge.entity.UserScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserScoreRepository extends JpaRepository<UserScore, Long> {

    Optional<UserScore> findByUser(User user);

    Optional<UserScore> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}
