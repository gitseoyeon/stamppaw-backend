package org.example.stamppaw_backend.badge.service;

import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.badge.entity.Badge;
import org.example.stamppaw_backend.badge.entity.TargetField;
import org.example.stamppaw_backend.badge.entity.UserScore;
import org.example.stamppaw_backend.badge.repository.UserScoreRepository;
import org.example.stamppaw_backend.common.exception.ErrorCode;
import org.example.stamppaw_backend.common.exception.StampPawException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BadgeEvaluatorComplete implements BadgeEvaluator {

    private final UserScoreRepository userScoreRepository;

    // Badge Rule 달성 여부
    @Override
    public boolean evaluate(Badge badge, Long userId) {
        if (badge.getRuleType() == null) return false;
        return evaluateRule(badge, userId);
    }

    @Override
    public int calculateProgress(Badge badge, Long userId) {
        if (badge.getRuleType() == null) return 0;
        return calculateRuleProgress(badge, userId);
    }

    // Rule Type Switch
    private boolean evaluateRule(Badge badge, Long userId) {
        return switch (badge.getRuleType()) {
            case COUNT -> evaluateCount(badge, userId);
            case SUM -> evaluateSum(badge, userId);
            case STREAK -> evaluateStreak(badge, userId);
            case TIME_RANGE -> evaluateTimeRange(badge, userId);
            case EXISTS -> evaluateExistsField(badge, userId);
        };
    }

    private int calculateRuleProgress(Badge badge, Long userId) {
        return switch (badge.getRuleType()) {
            case COUNT -> calculateCountProgress(badge, userId);
            case SUM -> calculateSumProgress(badge, userId);
            case STREAK -> calculateStreakProgress(badge, userId);
            case TIME_RANGE -> calculateTimeRangeProgress(badge, userId);
            case EXISTS -> evaluateExistsField(badge, userId) ? 100 : 0;
        };
    }

    // UserScore 조회 헬퍼
    private UserScore getScoreOrNull(Long userId) {
        return userScoreRepository.findByUserId(userId).orElse(null);
    }

    // ==========================================================
    // COUNT
    // ==========================================================
    private boolean evaluateCount(Badge badge, Long userId) {
        int value = getCountValue(badge.getTargetField(), userId);
        return value >= badge.getThreshold();
    }

    private int calculateCountProgress(Badge badge, Long userId) {
        int value = getCountValue(badge.getTargetField(), userId);
        double percent = (value / (double) badge.getThreshold()) * 100.0;
        return (int) Math.min(100, percent);
    }

    private int getCountValue(TargetField field, Long userId) {
        UserScore score = getScoreOrNull(userId);

        return switch (field) {

            case WALK_COUNT -> (score == null) ? 0 : score.getWalkCount();

            case PHOTO_COUNT -> (score == null) ? 0 : score.getWalkPhotoCount();

            case MEMO_COUNT -> (score == null) ? 0 : score.getWalkMemoCount();

            case POINT_SUM -> (score == null) ? 0 : score.getPointEarnedSum();

            case COMPANION_MATCH_COUNT -> (score == null) ? 0 : score.getCompanionMatchCount();

            default -> 0;
        };
    }

    // ==========================================================
    // SUM
    // ==========================================================
    private boolean evaluateSum(Badge badge, Long userId) {
        double value = getSumValue(badge.getTargetField(), userId);
        return value >= badge.getThreshold();
    }

    private int calculateSumProgress(Badge badge, Long userId) {
        double value = getSumValue(badge.getTargetField(), userId);
        double percent = (value / badge.getThreshold()) * 100.0;
        return (int) Math.min(100, percent);
    }

    private double getSumValue(TargetField field, Long userId) {
        UserScore score = getScoreOrNull(userId);

        return switch (field) {

            case WALK_DURATION -> {
                if (score == null) yield 0.0;
                yield (double) score.getWalkDurationSum();
            }

            case WALK_DISTANCE -> {
                if (score == null) yield 0.0;
                yield score.getWalkDistanceSum();
            }

            case POINT_SUM -> {
                if (score == null) yield 0.0;
                yield (double) score.getPointEarnedSum();
            }

            default -> 0.0;
        };
    }

    // ==========================================================
    // STREAK
    // ==========================================================
    private boolean evaluateStreak(Badge badge, Long userId) {
        int streak = getStreakValue(userId);
        return streak >= badge.getThreshold();
    }

    private int calculateStreakProgress(Badge badge, Long userId) {
        int streak = getStreakValue(userId);
        double percent = (streak / (double) badge.getThreshold()) * 100.0;
        return (int) Math.min(100, percent);
    }

    private int getStreakValue(Long userId) {
        UserScore score = getScoreOrNull(userId);
        if (score == null) return 0;
        return score.calculateMaxStreak();
    }

    // ==========================================================
    // TIME RANGE
    // ==========================================================
    private boolean evaluateTimeRange(Badge badge, Long userId) {
        int count = getTimeRangeValue(badge, userId);
        return count >= badge.getThreshold();
    }

    private int calculateTimeRangeProgress(Badge badge, Long userId) {
        int count = getTimeRangeValue(badge, userId);
        double percent = (count / (double) badge.getThreshold()) * 100.0;
        return (int) Math.min(100, percent);
    }

    private int getTimeRangeValue(Badge badge, Long userId) {
        Integer start = badge.getStartHour();
        Integer end = badge.getEndHour();

        if (start == null || end == null) {
            throw new StampPawException(ErrorCode.BADGE_RULE_MISSING_TIME_RANGE);
        }

        UserScore score = getScoreOrNull(userId);
        if (score == null) return 0;

        return score.countWalksInTimeRange(start, end);
    }

    // ==========================================================
    // EXISTS (User, Walk 등)
    // ==========================================================
    private boolean evaluateExistsField(Badge badge, Long userId) {

        return switch (badge.getTargetField()) {

            case USER_EXISTS -> userScoreRepository.existsByUserId(userId);
            // TODO: userRepository.existsById(userId) 로 교체 가능

            default -> false;
        };
    }
}
