package org.example.stamppaw_backend.parttime.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ApplyStatus {
    PENDING("신청중"),
    ACCEPTED("수락"),
    REJECTED("거부");

    private final String description;
}
