package org.example.stamppaw_backend.market.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentStatus {
    READY("결제준비"),
    IN_PROGRESS("결제진행중"),
    DONE("결제완료"),
    CANCELED("결제취소");

    private final String label;
}
