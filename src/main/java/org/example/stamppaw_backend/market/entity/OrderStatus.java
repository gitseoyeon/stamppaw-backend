package org.example.stamppaw_backend.market.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    ORDER("주문"),
    PAYED("결제 완료"),   // 결제 진행할때 업데이트, 고민중
    CANCELED("주문 취소");

    private final String label;
}