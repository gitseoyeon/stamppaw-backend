package org.example.stamppaw_backend.random.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.common.util.DistanceCalculator;
import org.example.stamppaw_backend.random.dto.RandomPointDto;
import org.example.stamppaw_backend.random.dto.UserLocationState;
import org.example.stamppaw_backend.random.generator.RandomPointGenerator;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RandomPointRedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    // 생성/삭제/방문 반경 설정
    private static final long TTL_MINUTES = 20;

    // 프론트에서 시야(표시) 100m 사용할 것. 서버는 생성/삭제/방문만 관리
    private static final double GENERATE_RADIUS_METERS = 300.0; // 생성 반경
    private static final double REMOVE_RADIUS_METERS   = 500.0; // 삭제 반경
    private static final double VISIT_RADIUS_METERS    = 30.0;  // 방문 판정 반경

    private String pointsKey(Long userId) {
        return "random_points:" + userId;
    }

    private String stateKey(Long userId) {
        return "user_location:" + userId;
    }

    /* ===========================
       위치 상태
       =========================== */

    public UserLocationState getState(Long userId) {
        Object o = redisTemplate.opsForValue().get(stateKey(userId));
        return (o instanceof UserLocationState s) ? s : null;
    }

    public void saveState(Long userId, UserLocationState state) {
        redisTemplate.opsForValue().set(stateKey(userId), state, 60, TimeUnit.MINUTES);
    }

    /* ===========================
       포인트 조회
       =========================== */

    public Map<Object, Object> rawPoints(Long userId) {
        return redisTemplate.opsForHash().entries(pointsKey(userId));
    }

    public List<RandomPointDto> getPoints(Long userId) {
        List<RandomPointDto> result = new ArrayList<>();
        rawPoints(userId).values().forEach(v ->
                result.add(objectMapper.convertValue(v, RandomPointDto.class))
        );
        return result;
    }

    /* ===========================
       포인트 생성 (UUID key)
       =========================== */

    public void addPoints(Long userId, double baseLat, double baseLng, int count) {
        String key = pointsKey(userId);

        for (int i = 0; i < count; i++) {
            RandomPointGenerator.LatLng pos =
                    RandomPointGenerator.generateRandomPoint(baseLat, baseLng, GENERATE_RADIUS_METERS);

            RandomPointDto dto = RandomPointDto.builder()
                    .lat(pos.lat())
                    .lng(pos.lng())
                    .visited(false)
                    .createdAt(LocalDateTime.now())
                    .build();

            String uuid = UUID.randomUUID().toString();
            redisTemplate.opsForHash().put(key, uuid, dto);
        }

        redisTemplate.expire(key, TTL_MINUTES, TimeUnit.MINUTES);
    }

    /* ===========================
       먼 포인트 제거 (재정렬 없음)
       =========================== */

    public void removeFar(Long userId, double currLat, double currLng) {
        String key = pointsKey(userId);
        Map<Object, Object> all = rawPoints(userId);

        for (Map.Entry<Object, Object> entry : all.entrySet()) {
            RandomPointDto dto = objectMapper.convertValue(entry.getValue(), RandomPointDto.class);

            double dist = DistanceCalculator.distanceMeters(
                    currLat, currLng,
                    dto.getLat(), dto.getLng()
            );

            if (dist > REMOVE_RADIUS_METERS) {
                redisTemplate.opsForHash().delete(key, entry.getKey());
            }
        }
    }

    /* ===========================
       방문 처리 (20m 안에 있는 포인트 삭제)
       =========================== */

    public int visitAndGetCount(Long userId, double currLat, double currLng) {
        String key = pointsKey(userId);
        Map<Object, Object> all = rawPoints(userId);

        int count = 0;

        for (Map.Entry<Object, Object> entry : all.entrySet()) {
            RandomPointDto dto = objectMapper.convertValue(entry.getValue(), RandomPointDto.class);

            double dist = DistanceCalculator.distanceMeters(
                    currLat, currLng,
                    dto.getLat(), dto.getLng()
            );

            if (dist <= VISIT_RADIUS_METERS) {
                redisTemplate.opsForHash().delete(key, entry.getKey());
                count++;
            }
        }

        return count;
    }
}
