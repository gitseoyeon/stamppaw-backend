package org.example.stamppaw_backend.market.repository;

import org.example.stamppaw_backend.market.entity.Category;
import org.example.stamppaw_backend.market.entity.Product;
import org.example.stamppaw_backend.market.entity.ProductStatus;
import org.example.stamppaw_backend.market.repository.projection.ProductListRow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<ProductListRow> findAllBy(Pageable pageable);

    Page<ProductListRow> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("""
    SELECT p 
    FROM Product p 
    WHERE p.status = :status
      AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')))
    ORDER BY p.id DESC
""")
    Page<ProductListRow> findByNameOrDescription(
            @Param("status") ProductStatus status,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    List<ProductListRow> findTop5ByStatusOrderByIdDesc(ProductStatus status);

    List<Product> findByStatus(ProductStatus status);

    List<ProductListRow> findByCategoryAndStatusOrderByIdDesc(Category category, ProductStatus status);

    @EntityGraph(attributePaths = {"images", "options"})
    @NonNull
    Optional<Product> findById(@NonNull Long id);
}
