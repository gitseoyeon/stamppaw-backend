package org.example.stamppaw_backend.market.repository.projection;

import org.example.stamppaw_backend.market.entity.Category;
import org.example.stamppaw_backend.market.entity.ProductStatus;

import java.math.BigDecimal;

public interface ProductListRow {
    Long getId();
    String getName();
    Category getCategory();
    ProductStatus getStatus();
    BigDecimal getPrice();
    String getMainImageUrl(); // 서브쿼리로 뽑은 대표이미지
}
