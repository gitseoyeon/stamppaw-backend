package org.example.stamppaw_backend.market.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ShippingStatus {
    READY("준비중"),
    SHIPPED("배송중"),
    DELIVERED("배송완료"),
    RETURN("반품요청");

    private final String label;
}
