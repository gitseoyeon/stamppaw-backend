package org.example.stamppaw_backend.market.dto.request;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCreateRequest {

    private Long cartId;                    // 주문을 생성할 장바구니 ID
    private List<Long> cartItemIds;         // 선택한 장바구니 아이템 ID 목록

    private String shippingName;
    private String shippingAddress;
    private String shippingMobile;
    private String paymentMethod;

}
