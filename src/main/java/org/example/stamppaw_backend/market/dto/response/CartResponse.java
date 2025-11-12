package org.example.stamppaw_backend.market.dto.response;

import lombok.*;
import org.example.stamppaw_backend.market.entity.Cart;
import org.example.stamppaw_backend.market.entity.CartItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponse {
    private Long cartId;
    private Long userId;
    private List<ItemDto> items;

    public static CartResponse fromEntity(Cart cart) {
        return CartResponse.builder()
                .cartId(cart.getId())
                .userId(cart.getUser().getId())
                .items(cart.getCartItems().stream()
                        .map(ItemDto::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ItemDto {

        private Long id;
        private Long productId;
        private String productName;
        private String optionSummary;
        private BigDecimal price; //상품 단가
        private Integer quantity;
        private BigDecimal subtotal; //상품 단가 * 수량 * + extra_price

        public static ItemDto fromEntity(CartItem item) {
            return ItemDto.builder()
                    .id(item.getId())
                    .productId(item.getProduct().getId())
                    .productName(item.getProduct().getName())
                    .optionSummary(item.getOptionSummary())
                    .price(item.getPrice())
                    .quantity(item.getQuantity())
                    .subtotal(item.getSubtotal())
                    .build();
        }
    }
}
