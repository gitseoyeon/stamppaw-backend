package org.example.stamppaw_backend.badge.service;

import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.badge.entity.UserScore;
import org.example.stamppaw_backend.badge.repository.UserScoreRepository;
import org.example.stamppaw_backend.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class UserScoreService {

    private final UserScoreRepository userScoreRepository;

    // 유저의 UserScore 조회 or 생성
    public UserScore getOrCreate(User user) {
        return userScoreRepository.findByUserId(user.getId())
                .orElseGet(() -> userScoreRepository.save(
                        UserScore.builder()
                                .user(user)
                                .walkCount(0)
                                .walkDistanceSum(0.0)
                                .walkDurationSum(0L)
                                .walkPhotoCount(0)
                                .walkMemoCount(0)
                                .pointEarnedSum(0)
                                .companionMatchCount(0)
                                .build()
                ));
    }

    // 산책 1회 종료 시 스코어 반영
    public void applyWalkCompleted(User user,
                                   double distanceMeters,
                                   long durationMinutes,
                                   boolean hasPhoto,
                                   boolean hasMemo,
                                   LocalDateTime startTime) {

        UserScore score = getOrCreate(user);

        score.increaseWalkCount();
        score.increaseWalkDistance(distanceMeters);
        score.increaseWalkDuration(durationMinutes);

        if (hasPhoto) {
            score.increasePhotoCount();
        }
        if (hasMemo) {
            score.increaseMemoCount();
        }

        // 날짜 / 시간대 정보도 함께 반영
        if (startTime != null) {
            score.addWalkDate(startTime.toLocalDate());
            score.increaseWalkCountForHour(startTime.getHour());
        }

        // @Transactional + dirty checking 으로 flush 되지만, 명시적으로 save 해도 OK
        userScoreRepository.save(score);
    }

    // 사진/메모만 반영 (기록 수정용)
    public void applyWalkCompleted(User user, boolean hasPhoto) {
        UserScore score = getOrCreate(user);

        if (hasPhoto) {
            score.increasePhotoCount();
        }

        userScoreRepository.save(score);
    }

    // 포인트 획득 시 반영
    public void addPoints(User user, int points) {
        if (points <= 0) return;
        UserScore score = getOrCreate(user);
        score.increasePoints(points);
        userScoreRepository.save(score);
    }

    // 동행 매칭 성공 시 반영
    public void increaseMatchCount(User user) {
        UserScore score = getOrCreate(user);
        score.increaseMatchCount();
        userScoreRepository.save(score);
    }
}
