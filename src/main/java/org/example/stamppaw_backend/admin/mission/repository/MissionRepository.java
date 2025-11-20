package org.example.stamppaw_backend.admin.mission.repository;

import org.example.stamppaw_backend.admin.mission.entity.Mission;
import org.example.stamppaw_backend.user_mission.entity.MissionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MissionRepository extends JpaRepository<Mission, Long> {
    boolean existsByType(MissionType type);

    @Query("SELECT DISTINCT m.type FROM Mission m")
    List<MissionType> findDistinctTypes();
}