package org.example.stamppaw_backend.admin.mission.repository;

import org.example.stamppaw_backend.admin.mission.entity.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionRepository extends JpaRepository<Mission, Long> {

}