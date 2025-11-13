package org.example.stamppaw_backend.market.repository.projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface OrderListRow {
    Long getOrderId();
    BigDecimal getTotalAmount();
    String getStatus();
    String getShippingStatus();
    LocalDateTime getRegisteredAt();
    LocalDateTime getModifiedAt();
    String getUsername();
}
