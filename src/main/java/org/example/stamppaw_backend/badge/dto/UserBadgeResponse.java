package org.example.stamppaw_backend.badge.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.stamppaw_backend.badge.entity.Badge;
import org.example.stamppaw_backend.badge.entity.BadgeCode;
import org.example.stamppaw_backend.badge.entity.UserBadge;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class UserBadgeResponse {

    private Long badgeId;
    private String name;
    private BadgeCode badgeCode;
    private String description;
    private String iconUrl;

    private boolean achieved;
    private int progress;
    private boolean representative;
    private LocalDateTime achievedAt;

    public static UserBadgeResponse from(Badge badge, UserBadge ub) {

        return UserBadgeResponse.builder()
                .badgeId(badge.getId())
                .name(badge.getName())
                .badgeCode(badge.getBadgeCode())
                .description(badge.getDescription())
                .iconUrl(badge.getIconUrl())

                .achieved(ub != null && ub.isAchieved())
                .progress(ub != null ? ub.getProgress() : 0)
                .representative(ub != null && ub.isRepresentative())
                .achievedAt(ub != null ? ub.getAchievedAt() : null)

                .build();
    }

}
