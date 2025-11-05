package org.example.stamppaw_backend.market.dto.response;


import org.example.stamppaw_backend.market.repository.projection.ProductListRow;

public record ProductListResponse(
        Long id,
        String name,
        String category,
        String status,
        String price,
        String mainImageUrl
) {
    public static ProductListResponse fromRow(ProductListRow row) {
        return new ProductListResponse(
                row.getId(),
                row.getName(),
                row.getCategory() != null ? row.getCategory().name() : null,
                row.getStatus()   != null ? row.getStatus().name()   : null,
                row.getPrice()    != null ? row.getPrice().toPlainString() : null,
                row.getMainImageUrl()
        );
    }
}

