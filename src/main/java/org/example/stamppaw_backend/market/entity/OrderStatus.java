package org.example.stamppaw_backend.market.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    ORDER("주문"),
    CANCELED("주문취소");

    private final String label;
}