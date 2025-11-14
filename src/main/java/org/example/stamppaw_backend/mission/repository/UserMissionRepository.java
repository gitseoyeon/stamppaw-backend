package org.example.stamppaw_backend.mission.repository;

import org.example.stamppaw_backend.mission.entity.UserMission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserMissionRepository extends JpaRepository<UserMission, Long> {

    List<UserMission> findByUserId(Long userId);

    List<UserMission> findByUserIdAndStatusFalse(Long userId);

    @Query("SELECT um FROM UserMission um JOIN FETCH um.mission WHERE um.user.id = :userId AND um.status = false")
    List<UserMission> findByUserIdAndStatusFalseWithMission(@Param("userId") Long userId);

}