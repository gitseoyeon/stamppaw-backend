package org.example.stamppaw_backend.market.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResponse {

    private Long orderId;

    private String status;
    private BigDecimal totalAmount;
    private BigDecimal shippingFee;
    private String shippingName;
    private String shippingAddress;
    private String shippingMobile;
    private String shippingStatus;
    private LocalDateTime registeredAt;

    private PaymentInfo payment;

    private List<OrderItemResponse> items;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentInfo {
        private String paymentKey;
        private String status;
        private String tossOrderId;
        private String method;
        private LocalDateTime approvedAt;
        private String receiptUrl;
    }
}
