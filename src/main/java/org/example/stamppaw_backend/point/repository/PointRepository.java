package org.example.stamppaw_backend.point.repository;

import org.example.stamppaw_backend.point.dto.PointResponse;
import org.example.stamppaw_backend.point.entity.Point;
import org.example.stamppaw_backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PointRepository extends JpaRepository<Point, Long> {

    List<PointResponse> findAllByUser(User user);

    // 가장 최근 포인트 로그 조회
    Optional<PointResponse> findTopByUserOrderByIdDesc(User user);
}