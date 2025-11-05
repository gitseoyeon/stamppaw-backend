package org.example.stamppaw_backend.market.repository;

import org.example.stamppaw_backend.market.dto.response.ProductListResponse;
import org.example.stamppaw_backend.market.entity.Product;
import org.example.stamppaw_backend.market.entity.ProductStatus;
import org.example.stamppaw_backend.market.repository.projection.ProductListRow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // 상세용: 엔티티 1개만 (컬렉션은 LAZY로 나중에)
    @Query("select p from Product p where p.id = :id")
    Optional<Product> findDetailById(@Param("id") Long id);

    // 관리자: 전체 목록 + 페이징 (대표 이미지 1개만)
    @Query(
            value = """
            select p.id as id,
                   p.name as name,
                   p.category as category,
                   p.status as status,
                   p.price as price,
                   (
                     select pi.imageUrl
                     from ProductImage pi
                     where pi.product = p
                     order by pi.isMain desc,
                              coalesce(pi.sort, 0) asc,
                              pi.id asc
                     fetch first 1 row only
                   ) as mainImageUrl
            from Product p
            """,
            countQuery = "select count(p) from Product p"
    )
    Page<ProductListRow> findAllForAdmin(Pageable pageable);

    // 이름 검색 + 페이징 (대표 이미지 1개만)
    @Query("""
           select p.id as id,
                  p.name as name,
                  p.category as category,
                  p.status as status,
                  p.price as price,
                  (
                    select pi.imageUrl
                    from ProductImage pi
                    where pi.product = p
                    order by pi.isMain desc,
                             coalesce(pi.sort, 0) asc,
                             pi.id asc
                  ) as mainImageUrl
           from Product p
           where (:name is null or lower(p.name) like lower(concat('%', :name, '%')))
           """)
    Page<ProductListRow> findList(@Param("name") String name, Pageable pageable);

    // 상태 + 이름 필터 (선택)
    @Query("""
           select p.id as id,
                  p.name as name,
                  p.category as category,
                  p.status as status,
                  p.price as price,
                  (
                    select pi.imageUrl
                    from ProductImage pi
                    where pi.product = p
                    order by pi.isMain desc,
                             coalesce(pi.sort, 0) asc,
                             pi.id asc
                  ) as mainImageUrl
           from Product p
           where (:status is null or p.status = :status)
             and (:name   is null or lower(p.name) like lower(concat('%', :name, '%')))
           """)
    Page<ProductListRow> findListByStatusAndName(@Param("status") ProductStatus status,
                                                 @Param("name") String name,
                                                 Pageable pageable);


}

