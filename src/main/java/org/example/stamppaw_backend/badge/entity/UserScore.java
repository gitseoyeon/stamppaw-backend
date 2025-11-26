package org.example.stamppaw_backend.badge.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.stamppaw_backend.user.entity.User;

import java.time.LocalDate;
import java.util.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "user_score")
public class UserScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    // ============================
    // 산책 관련 누적 스코어
    // ============================
    private int walkCount;          // 산책 횟수
    private double walkDistanceSum; // 총 산책 거리 (meter)
    private long walkDurationSum;   // 총 산책 시간 (minute)

    private int walkPhotoCount;     // 산책 사진 업로드 수
    private int walkMemoCount;      // 산책 메모 수

    // 날짜별 산책 기록 (연속 산책용)
    @Builder.Default
    @ElementCollection
    @CollectionTable(
            name = "user_score_walk_dates",
            joinColumns = @JoinColumn(name = "user_score_id")
    )
    @Column(name = "walk_date")
    private Set<LocalDate> walkDates = new HashSet<>();

    // 시간대별 산책 횟수 (시간대 뱃지용)
    @Builder.Default
    @ElementCollection
    @CollectionTable(
            name = "user_score_walk_hour_counts",
            joinColumns = @JoinColumn(name = "user_score_id")
    )
    @MapKeyColumn(name = "hour")
    @Column(name = "walk_count")
    private Map<Integer, Integer> walkCountByHour = new HashMap<>();

    // 포인트 / 매칭 등 누적 스코어
    private int pointEarnedSum;      // 누적 획득 포인트
    private int companionMatchCount; // 동행 매칭 횟수

    // 누적 증가 메서드
    public void increaseWalkCount() {
        this.walkCount += 1;
    }

    public void increaseWalkDistance(double distance) {
        this.walkDistanceSum += distance;
    }

    public void increaseWalkDuration(long duration) {
        this.walkDurationSum += duration;
    }

    public void increasePhotoCount() {
        this.walkPhotoCount += 1;
    }

    public void increaseMemoCount() {
        this.walkMemoCount += 1;
    }

    public void increasePoints(int points) {
        this.pointEarnedSum += points;
    }

    public void increaseMatchCount() {
        this.companionMatchCount += 1;
    }

    // 날짜/시간대 업데이트용 헬퍼
    public void addWalkDate(LocalDate date) {
        if (date != null) {
            this.walkDates.add(date); // Set 이라 중복 방지
        }
    }

    public void increaseWalkCountForHour(int hour) {
        if (hour < 0 || hour > 23) return;
        this.walkCountByHour.merge(hour, 1, Integer::sum);
    }

    // 시간대 산책 횟수 계산
    public int countWalksInTimeRange(int startHour, int endHour) {
        if (walkCountByHour == null || walkCountByHour.isEmpty()) {
            return 0;
        }

        return walkCountByHour.entrySet().stream()
                .filter(e -> e.getKey() >= startHour && e.getKey() <= endHour)
                .mapToInt(Map.Entry::getValue)
                .sum();
    }

    // 최대 연속 산책일(STREAK) 계산
    public int calculateMaxStreak() {
        if (walkDates == null || walkDates.isEmpty()) return 0;

        // 중복 제거 + 정렬
        List<LocalDate> sorted = walkDates.stream()
                .distinct()
                .sorted()
                .toList();

        int streak = 1;
        int maxStreak = 1;

        for (int i = 1; i < sorted.size(); i++) {
            if (sorted.get(i).minusDays(1).equals(sorted.get(i - 1))) {
                streak++;
                maxStreak = Math.max(maxStreak, streak);
            } else {
                streak = 1;
            }
        }
        return maxStreak;
    }
}
