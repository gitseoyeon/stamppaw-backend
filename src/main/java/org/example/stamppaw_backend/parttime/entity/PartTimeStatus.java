package org.example.stamppaw_backend.parttime.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PartTimeStatus {
    ONGOING("모집 중"),
    CLOSED("마감");

    private final String description;
}
