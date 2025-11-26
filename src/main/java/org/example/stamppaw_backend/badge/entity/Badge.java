package org.example.stamppaw_backend.badge.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ex) WALK_MASTER, PHOTO_KING 같은 코드
    @Column(name = "badge_code", nullable = false, unique = true)
    private BadgeCode badgeCode;

    private String name;

    private String description;

    private String category;

    private String iconUrl;

    @Column(nullable = false)
    private boolean active;

    // ==============================
    //  룰 관련 필드
    // ==============================
    @Enumerated(EnumType.STRING)
    private RuleType ruleType;       // COUNT / SUM / STREAK / TIME_RANGE / EXISTS 등

    @Enumerated(EnumType.STRING)
    private TargetEntity targetEntity;  // WALK / POINT / USER 등

    @Enumerated(EnumType.STRING)
    private TargetField targetField;    // DISTANCE / DURATION / COUNT 등

    private Integer threshold;      // 기준 값 (예: 10회, 500m, 30분 등)

    private Integer startHour;      // TIME_RANGE 시작 시간 (0~23)
    private Integer endHour;        // TIME_RANGE 끝 시간 (0~23)
}
