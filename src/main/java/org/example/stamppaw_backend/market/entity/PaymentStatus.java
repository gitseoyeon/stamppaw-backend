package org.example.stamppaw_backend.market.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentStatus {
    READY("결제 준비"),
    IN_PROGRESS("결제 진행 중"),
    PAYED("결제 완료"),
    CANCELED("결제 취소");

    private final String label;
}
