package org.example.stamppaw_backend.badge.service;

import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.badge.dto.UserBadgeResponse;
import org.example.stamppaw_backend.badge.entity.Badge;
import org.example.stamppaw_backend.badge.entity.UserBadge;
import org.example.stamppaw_backend.badge.repository.BadgeRepository;
import org.example.stamppaw_backend.badge.repository.UserBadgeRepository;
import org.example.stamppaw_backend.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserBadgeService {

    private final BadgeRepository badgeRepository;
    private final UserBadgeRepository userBadgeRepository;
    private final BadgeEvaluator badgeEvaluator;

    /**
     * 🔵 전체 뱃지 + 유저 진행 상태
     */
    @Transactional(readOnly = true)
    public List<UserBadgeResponse> getUserBadges(Long userId) {

        List<Badge> allBadges = badgeRepository.findAllByOrderByIdAsc();
        List<UserBadge> ownedBadges = userBadgeRepository.findAllByUserId(userId);

        Map<Long, UserBadge> map = ownedBadges.stream()
                .collect(Collectors.toMap(
                        ub -> ub.getBadge().getId(),
                        ub -> ub
                ));

        return allBadges.stream()
                .map(badge -> UserBadgeResponse.from(badge, map.get(badge.getId())))
                .toList();
    }

    /**
     * 🟠 뱃지 평가 (진행도 + 달성)
     *  - 진행도: 아직 미달성인 경우에만 계산
     *  - 한번 달성(achieved=true)이 되면 진행도는 100%로 고정 (데이터 삭제/변경 영향 없음)
     */
    public void evaluateBadges(User user) {

        List<Badge> badges = badgeRepository.findAll();

        for (Badge badge : badges) {

            UserBadge ub = userBadgeRepository
                    .findByUserIdAndBadgeId(user.getId(), badge.getId())
                    .orElseGet(() -> userBadgeRepository.save(
                            UserBadge.builder()
                                    .user(user)
                                    .badge(badge)
                                    .progress(0)
                                    .achieved(false)
                                    .representative(false)
                                    .build()
                    ));

            // ✅ 이미 달성한 뱃지는 진행도 100% 유지
            int progress = ub.isAchieved()
                    ? 100
                    : badgeEvaluator.calculateProgress(badge, user.getId());

            boolean achievedNow = badgeEvaluator.evaluate(badge, user.getId());

            ub.updateProgress(progress);

            // 🔵 이번에 새로 달성한 경우에만 achieve()
            if (achievedNow && !ub.isAchieved()) {
                ub.achieve();
            }

            userBadgeRepository.save(ub);
        }
    }

    /**
     * 🟡 대표 뱃지 설정
     */
    public void setRepresentative(User user, Long badgeId) {

        UserBadge ub = userBadgeRepository
                .findByUserIdAndBadgeId(user.getId(), badgeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 뱃지 없음"));

        if (!ub.isAchieved()) {
            throw new IllegalStateException("달성하지 않은 뱃지를 대표로 지정할 수 없습니다.");
        }

        userBadgeRepository
                .findByUserIdAndRepresentativeTrue(user.getId())
                .ifPresent(UserBadge::cancelRepresentative);

        ub.setRepresentative();
    }

    public void clearRepresentative(User user) {
        userBadgeRepository.findByUserIdAndRepresentativeTrue(user.getId())
                .ifPresent(UserBadge::cancelRepresentative);
    }
}
