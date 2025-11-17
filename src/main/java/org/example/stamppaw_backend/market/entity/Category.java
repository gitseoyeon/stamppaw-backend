package org.example.stamppaw_backend.market.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    CLOTHING_GOODS("의류굿즈"),
    ACRYLIC_GOODS("아크릴굿즈"),
    CAP("모자");

    private final String label;
}

