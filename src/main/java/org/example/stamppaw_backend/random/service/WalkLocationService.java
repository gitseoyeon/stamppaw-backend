package org.example.stamppaw_backend.random.service;

import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.common.util.DistanceCalculator;
import org.example.stamppaw_backend.point.dto.PointRequest;
import org.example.stamppaw_backend.point.service.PointService;
import org.example.stamppaw_backend.random.dto.LocationUpdateRequest;
import org.example.stamppaw_backend.random.dto.LocationUpdateResponse;
import org.example.stamppaw_backend.random.dto.RandomPointDto;
import org.example.stamppaw_backend.random.dto.UserLocationState;
import org.example.stamppaw_backend.user_mission.entity.MissionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WalkLocationService {

    private final RandomPointRedisService redis;
    private final PointService pointService;

    // 규칙
    private static final double NEW_POINTS_DISTANCE_METERS = 30.0; // 30m 이상 이동 시 새 포인트 생성
    private static final int MIN_POINTS_THRESHOLD = 3;             // 3개 이하이면 보충
    private static final int ADD_WHEN_LOW = 3;                     // 부족할 때 추가 개수
    private static final int ADD_ON_MOVE = 1;                      // 이동 시 추가 개수

    @Transactional
    public LocationUpdateResponse handle(Long userId, LocationUpdateRequest request) {

        double currLat = request.getLat();
        double currLng = request.getLng();
        long currTime = request.getTimestampMillis();

        boolean generatedNewPoints = false;
        int visitedCount = 0;
        int reward = 0;

        // 1) 이전 위치 상태 조회
        UserLocationState prevState = redis.getState(userId);

        double movedDistance = 0.0;
        double accumulatedDistance = 0.0;

        if (prevState != null) {
            movedDistance = DistanceCalculator.distanceMeters(
                    prevState.getLat(), prevState.getLng(),
                    currLat, currLng
            );
            accumulatedDistance = prevState.getAccumulatedDistanceMeters() + movedDistance;
        }

        // 2) 너무 멀어진 포인트 제거 (500m 밖)
        redis.removeFar(userId, currLat, currLng);

        // 3) 방문 처리 (20m 안에 있는 포인트 삭제 + 포인트 지급)
        visitedCount = redis.visitAndGetCount(userId, currLat, currLng);

        // 🚨 산책 중이 아닐 경우 보상 금지
        Long walkId = request.getWalkId();
        boolean walking = (walkId != null);

        // 3-1) 포인트 보상
        if (walking && visitedCount > 0) {
            reward = visitedCount * 1;
            pointService.addPoint(
                    userId,
                    new PointRequest(reward, MissionType.RANDOM_POINT_VISIT)
            );
        }

        // 4) 현재 포인트 수 확인
        List<RandomPointDto> points = redis.getPoints(userId);

        // 4-1) 포인트가 너무 적으면 (3개 이하) → 3개 보충
        if (points.size() <= MIN_POINTS_THRESHOLD) {
            redis.addPoints(userId, currLat, currLng, ADD_WHEN_LOW);
            generatedNewPoints = true;
        }

        // 4-2) 누적 이동거리 ≥ 30m → 1개 추가 생성
        if (accumulatedDistance >= NEW_POINTS_DISTANCE_METERS) {
            redis.addPoints(userId, currLat, currLng, ADD_ON_MOVE);
            accumulatedDistance = 0.0;
            generatedNewPoints = true;
        }

        // 5) 위치 상태 저장
        UserLocationState newState = UserLocationState.builder()
                .Lat(currLat)
                .Lng(currLng)
                .TimestampMillis(currTime)
                .accumulatedDistanceMeters(accumulatedDistance)
                .build();
        redis.saveState(userId, newState);

        // 6) 최신 포인트 다시 조회
        List<RandomPointDto> updatedPoints = redis.getPoints(userId);

        // 7) 응답
        return LocationUpdateResponse.builder()
                .speedValid(true)
                .generatedNewPoints(generatedNewPoints)
                .visitedPointCount(visitedCount)
                .points(updatedPoints)
                .reward(reward)
                .build();
    }
}
