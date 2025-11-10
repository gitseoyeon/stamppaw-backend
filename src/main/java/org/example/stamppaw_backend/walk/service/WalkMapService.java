package org.example.stamppaw_backend.walk.service;

import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.common.exception.ErrorCode;
import org.example.stamppaw_backend.common.exception.StampPawException;
import org.example.stamppaw_backend.walk.dto.request.WalkPointRequest;
import org.example.stamppaw_backend.walk.dto.response.WalkPointResponse;
import org.example.stamppaw_backend.walk.entity.Walk;
import org.example.stamppaw_backend.walk.entity.WalkPoint;
import org.example.stamppaw_backend.walk.repository.WalkPointRepository;
import org.example.stamppaw_backend.walk.repository.WalkRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WalkMapService {

    private final WalkRepository walkRepository;
    private final WalkPointRepository walkPointRepository;

    // 버퍼 업로드
    @Transactional
    public void addPoints(Long walkId, List<WalkPointRequest> requests) {
        Walk walk = walkRepository.findById(walkId)
                .orElseThrow(() -> new StampPawException(ErrorCode.WALK_NOT_FOUND));

        for (WalkPointRequest req : requests) {
            WalkPoint point = WalkPoint.builder()
                    .lat(req.getLat())
                    .lng(req.getLng())
                    .timestamp(req.getTimestamp())
                    .build();
            walkPointRepository.save(point);
        }
    }

    // 좌표 추가
    @Transactional
    public void addPoint(Long walkId, Double lat, Double lng, LocalDateTime timestamp) {
        Walk walk = walkRepository.findById(walkId)
                .orElseThrow(() -> new StampPawException(ErrorCode.WALK_NOT_FOUND));

        // 프론트에서 timestamp가 null이면 서버 현재 시간으로 대체
        LocalDateTime resolvedTimestamp = (timestamp != null) ? timestamp : LocalDateTime.now();

        WalkPoint point = WalkPoint.builder()
                .walk(walk)
                .lat(lat)
                .lng(lng)
                .timestamp(resolvedTimestamp)
                .build();

        walkPointRepository.save(point);
    }

    // 거리 계산 (Haversine 공식)
    public double calcTotalDistance(List<WalkPointResponse> points) {
        if (points == null || points.size() < 2) return 0.0;

        double total = 0.0;
        for (int i = 1; i < points.size(); i++) {
            WalkPointResponse p1 = points.get(i - 1);
            WalkPointResponse p2 = points.get(i);
            total += calcDistance(p1.getLat(), p1.getLng(), p2.getLat(), p2.getLng());
        }
        return total;
    }

    private double calcDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371000;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
