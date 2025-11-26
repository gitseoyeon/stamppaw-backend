package org.example.stamppaw_backend.admin.badge.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.stamppaw_backend.badge.entity.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class BadgeRequest {

    private BadgeCode badgeCode;
    private String name;
    private String description;
    private String category;

    private MultipartFile icon;

    // 수정 시만 사용. 생성일 때는 null → 자동 true
    private Boolean active;

    private RuleType ruleType;
    private TargetEntity targetEntity;
    private TargetField targetField;

    private Integer threshold;
    private Integer startHour;
    private Integer endHour;

    public Badge toEntity(String iconUrl) {
        return Badge.builder()
                .badgeCode(this.badgeCode)
                .name(this.name)
                .description(this.description)
                .category(this.category)
                .iconUrl(iconUrl)
                .active(this.active != null ? this.active : true)
                .ruleType(this.ruleType)
                .targetEntity(this.targetEntity)
                .targetField(this.targetField)
                .threshold(this.threshold)
                .startHour(this.startHour)
                .endHour(this.endHour)
                .build();
    }

    public void applyTo(Badge badge, String iconUrl) {

        badge.setBadgeCode(this.badgeCode);
        badge.setName(this.name);
        badge.setDescription(this.description);
        badge.setCategory(this.category);

        if (iconUrl != null) {
            badge.setIconUrl(iconUrl);
        }
        if (this.active != null) {
            badge.setActive(this.active);
        }

        badge.setRuleType(this.ruleType);
        badge.setTargetEntity(this.targetEntity);
        badge.setTargetField(this.targetField);
        badge.setThreshold(this.threshold);
        badge.setStartHour(this.startHour);
        badge.setEndHour(this.endHour);
    }

    public static BadgeRequest fromEntity(Badge b) {
        BadgeRequest r = new BadgeRequest();
        r.setBadgeCode(b.getBadgeCode());
        r.setName(b.getName());
        r.setDescription(b.getDescription());
        r.setCategory(b.getCategory());
        r.setActive(b.isActive());
        r.setRuleType(b.getRuleType());
        r.setTargetEntity(b.getTargetEntity());
        r.setTargetField(b.getTargetField());
        r.setThreshold(b.getThreshold());
        r.setStartHour(b.getStartHour());
        r.setEndHour(b.getEndHour());
        return r;
    }


}