package org.example.stamppaw_backend.admin.market.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.example.stamppaw_backend.market.entity.Category;       // ← 실제 패키지에 맞게 수정
import org.example.stamppaw_backend.market.entity.ProductStatus;  // ← 실제 패키지에 맞게 수정

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCreateRequest {

    @NotNull                   // Enum은 @NotBlank 대신 @NotNull 사용
    private Category category;

    @NotBlank
    @Size(max = 200)
    private String name;

    @Size(max = 10_000)
    private String description;

    @NotNull
    @Digits(integer = 16, fraction = 2)
    @PositiveOrZero
    private BigDecimal price;

    @NotNull
    private ProductStatus status;

    @Valid
    @Builder.Default
    private List<ImageDto> images = new ArrayList<>();

    @Valid
    @Builder.Default
    private List<OptionDto> options = new ArrayList<>();

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ImageDto {

        //@NotBlank 스토리지 URL 또는 공개 URL
        @Size(max = 2048)
        private String imageUrl;

        private Boolean isMain;

        @Builder.Default
        private Integer sort = 0;
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OptionDto {

        //@NotBlank
        @Size(max = 100)
        private String name;

       // @NotBlank
        @Size(max = 100)
        private String value;

        @Digits(integer = 16, fraction = 2)
        @PositiveOrZero
        @Builder.Default
        private BigDecimal extraPrice = BigDecimal.ZERO;
    }
}

