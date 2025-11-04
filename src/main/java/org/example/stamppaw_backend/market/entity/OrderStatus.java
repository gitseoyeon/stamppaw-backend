package org.example.stamppaw_backend.market.entity;

public enum OrderStatus {
    ORDER,   //주문
    PAYED,   // 결제 완료
    READY,   // 상품 준비 중
    SHIPPED, // 배송 중
    CANCEL,  // 주문 취소
    REFUND   //환불
}
