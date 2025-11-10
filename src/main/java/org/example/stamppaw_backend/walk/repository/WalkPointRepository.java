package org.example.stamppaw_backend.walk.repository;

import org.example.stamppaw_backend.walk.entity.WalkPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WalkPointRepository extends JpaRepository<WalkPoint, Long> {
    @Query(value = "SELECT * FROM walk_points WHERE walk_id = :walkId ORDER BY timestamp ASC", nativeQuery = true)
    List<WalkPoint> findByWalkIdOrderByTimestampAsc(@Param("walkId") Long walkId);
}
