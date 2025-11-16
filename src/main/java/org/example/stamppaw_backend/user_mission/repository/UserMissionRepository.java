package org.example.stamppaw_backend.user_mission.repository;

import org.example.stamppaw_backend.user_mission.entity.MissionType;
import org.example.stamppaw_backend.user_mission.entity.UserMission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface UserMissionRepository extends JpaRepository<UserMission, Long> {

    List<UserMission> findByUserId(Long userId);

    List<UserMission> findByUserIdAndStatusFalse(Long userId);

    @Query("SELECT um FROM UserMission um JOIN FETCH um.mission WHERE um.user.id = :userId AND um.status = false")
    List<UserMission> findByUserIdAndStatusFalseWithMission(@Param("userId") Long userId);

    // 미션 관련
    boolean existsByUserIdAndMission_TypeAndStartDate(Long userId, MissionType type, LocalDate date);
    boolean existsByUserIdAndMission_TypeAndStatusFalse(Long userId, MissionType type);
}