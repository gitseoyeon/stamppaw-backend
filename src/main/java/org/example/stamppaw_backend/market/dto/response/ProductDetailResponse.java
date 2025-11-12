package org.example.stamppaw_backend.market.dto.response;

import lombok.Builder;
import org.example.stamppaw_backend.market.entity.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Builder
public record ProductDetailResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Category category,
        ProductStatus status,
        String mainImageUrl,
        List<ProductImage> images,
        Set<ProductOption> options
) {
    public static ProductDetailResponse fromEntity(Product product) {
        return ProductDetailResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .category(product.getCategory())
                .status(product.getStatus())
                .mainImageUrl(product.getMainImageUrl())
                .images(product.getImages())
                .options(product.getOptions())
                .build();
    }
}
