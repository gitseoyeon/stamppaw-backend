package org.example.stamppaw_backend.market.repository;

import org.example.stamppaw_backend.market.entity.Category;
import org.example.stamppaw_backend.market.entity.Product;
import org.example.stamppaw_backend.market.entity.ProductStatus;
import org.example.stamppaw_backend.market.repository.projection.ProductListRow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // 상세조회 (이미지, 옵션 포함)
    @Query("""
        select distinct p from Product p
        left join fetch p.images
        left join fetch p.options
        where p.id = :id
    """)
    Optional<Product> findDetailById(@Param("id") Long id);

    //관리자: 전체 목록 (대표이미지만, 자동 projection)
    Page<ProductListRow> findAllBy(Pageable pageable);

    //관리자: 이름 검색 (대소문자 구분 없음)
    Page<ProductListRow> findByNameContainingIgnoreCase(String name, Pageable pageable);

    //프런트: SERVICE 상태 + 이름 검색(optional)
    Page<ProductListRow> findByStatusAndNameContainingIgnoreCaseOrderByIdDesc(
            ProductStatus status,
            String name,
            Pageable pageable
    );

    // 프런트: 카테고리 + 상태별 목록
    List<ProductListRow> findByCategoryAndStatusOrderByIdDesc(Category category, ProductStatus status);


    List<Product> findByStatusOrderByIdDesc(ProductStatus status, Pageable pageable);


    @Query("""
    SELECT p.mainImageUrl
    FROM Product p
    ORDER BY p.id DESC
    """)
    List<String> findLatestMainImageUrls(Pageable pageable);

}

