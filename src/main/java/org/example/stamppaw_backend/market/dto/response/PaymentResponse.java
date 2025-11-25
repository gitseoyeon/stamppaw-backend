package org.example.stamppaw_backend.market.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.example.stamppaw_backend.market.entity.Payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class PaymentResponse {
    private Long paymentId;
    private String tossOrderId;
    private String orderName;
    private BigDecimal amount;
    private String status;
    private Long orderId;
    private LocalDateTime approvedAt;
    private String receiptUrl;

    public static PaymentResponse fromEntity(Payment payment) {
        return PaymentResponse.builder()
                .paymentId(payment.getId())
                .tossOrderId(payment.getTossOrderId())
                .orderName(payment.getOrderName())
                .amount(payment.getAmount())
                .status(payment.getStatus().name())
                .orderId(payment.getOrder().getId())
                .approvedAt(payment.getApprovedAt())
                .receiptUrl(payment.getReceiptUrl())
                .build();
    }
}

