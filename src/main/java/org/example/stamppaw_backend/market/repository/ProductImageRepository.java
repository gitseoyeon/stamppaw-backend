package org.example.stamppaw_backend.market.repository;

import org.example.stamppaw_backend.market.entity.ProductImage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    @Query("""
    SELECT pi
    FROM ProductImage pi
    WHERE pi.isMain = true
      AND pi.product.status = 'SERVICE'
    ORDER BY pi.product.id DESC
    """)
    List<ProductImage> findLatestServiceMainImages(Pageable pageable);

    @Query("""
    SELECT pi.imageUrl
    FROM ProductImage pi
    WHERE pi.isMain = true
    ORDER BY pi.product.id DESC
    """)
    List<String> findLatestMainImageUrls(Pageable pageable);

}
