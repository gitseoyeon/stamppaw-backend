package org.example.stamppaw_backend.badge.entity;

import java.util.Set;

public enum TargetField {

    // ===== WALK =====
    WALK_DISTANCE(Set.of(TargetEntity.WALK)),
    WALK_DURATION(Set.of(TargetEntity.WALK)),
    WALK_COUNT(Set.of(TargetEntity.WALK)),
    WALK_STREAK(Set.of(TargetEntity.WALK)),
    PHOTO_COUNT(Set.of(TargetEntity.WALK)),
    MEMO_COUNT(Set.of(TargetEntity.WALK)),

    // ===== POINT =====
    POINT_SUM(Set.of(TargetEntity.POINT)),
    POINT_COUNT(Set.of(TargetEntity.POINT)),

    // ===== USER =====
    USER_EXISTS(Set.of(TargetEntity.USER)),

    // ===== COMPANION =====
    COMPANION_MATCH_COUNT(Set.of(TargetEntity.COMPANION));

    private final Set<TargetEntity> applicableEntities;

    TargetField(Set<TargetEntity> applicableEntities) {
        this.applicableEntities = applicableEntities;
    }

    public boolean isApplicable(TargetEntity entity) {
        return applicableEntities.contains(entity);
    }

    public Set<TargetEntity> getApplicableEntities() {
        return applicableEntities;
    }
}
