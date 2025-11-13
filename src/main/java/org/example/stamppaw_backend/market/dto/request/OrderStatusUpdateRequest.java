package org.example.stamppaw_backend.market.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.example.stamppaw_backend.market.entity.OrderStatus;
import org.example.stamppaw_backend.market.entity.ShippingStatus;

@Getter
@Setter
public class OrderStatusUpdateRequest {
    private Long orderId;
    private OrderStatus orderStatus;
    private ShippingStatus shippingStatus;
}
