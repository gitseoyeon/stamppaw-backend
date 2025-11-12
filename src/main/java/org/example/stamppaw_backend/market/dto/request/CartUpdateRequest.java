package org.example.stamppaw_backend.market.dto.request;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.Calendar;

@Getter
public class CartUpdateRequest {
    private Long cartItemId; // 수정 대상
    private Integer quantity; // 변경할 수량

    private BigDecimal subtotal;
    private BigDecimal extraPrice; //상품의 옵션별 추가금
}
