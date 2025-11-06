package org.example.stamppaw_backend.market.dto.response;


import org.example.stamppaw_backend.market.entity.Product;
import org.example.stamppaw_backend.market.repository.projection.ProductListRow;

public record ProductListResponse(
        Long id,
        String name,
        String category,
        String status,
        String price,
        String mainImageUrl
) {
    public static ProductListResponse fromEntity(Product product) {
        return new ProductListResponse(
                product.getId(),
                product.getName(),
                product.getCategory() != null ? product.getCategory().getLabel() : null, // enum label 출력
                product.getStatus() != null ? product.getStatus().name() : null,
                product.getPrice() != null ? product.getPrice().toPlainString() : null, // BigDecimal → String
                product.getMainImageUrl()
        );
    }

    public static ProductListResponse fromRow(ProductListRow row) {
        return new ProductListResponse(
                row.getId(),
                row.getName(),
                row.getCategory() != null ? row.getCategory().getLabel() : null,
                row.getStatus()   != null ? row.getStatus().name()   : null,
                row.getPrice()    != null ? row.getPrice().toPlainString() : null,
                row.getMainImageUrl()
        );
    }

}

