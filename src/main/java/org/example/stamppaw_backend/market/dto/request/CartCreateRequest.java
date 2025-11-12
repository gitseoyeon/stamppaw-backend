package org.example.stamppaw_backend.market.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartCreateRequest {
    @NotNull
    private Long userId; // 어떤 사용자의 장바구니인지

    @Valid
    @Builder.Default
    private List<ItemDto> items = new ArrayList<>();

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ItemDto {

        @NotNull
        private Long productId;

        private String optionSummary; //(예: "색상: Red / 사이즈: M")

        @NotNull
        @Min(1)
        private Integer quantity;

        @NotNull
        private BigDecimal price;

        private String userImageUrl;
    }
}
