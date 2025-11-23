package org.example.stamppaw_backend.market.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.stamppaw_backend.market.entity.OrderItem;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponse {
    private Long itemId;
    private String optionSummary;
    private Long productId;
    private String productName;
    private String mainImageUrl;
    private int quantity;
    private BigDecimal price;
    private BigDecimal subtotal;
    private String userImageUrl;

    public static OrderItemResponse fromEntity(OrderItem item) {
        return OrderItemResponse.builder()
                .itemId(item.getId())
                .optionSummary(item.getOptionSummary())
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .mainImageUrl(item.getProduct().getMainImageUrl())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .subtotal(item.getSubtotal())
                .userImageUrl(item.getUserImageUrl())
                .build();
    }
}
