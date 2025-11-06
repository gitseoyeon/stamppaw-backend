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

    @Query("""
        select distinct p from Product p
        left join fetch p.images
        left join fetch p.options
        where p.id = :id
    """)
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

    // 관리자 - 이름 검색 + 페이징 (대표 이미지 1개만)
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

    // 프런트 - status = SERVICE 만  + 이름 검색(옵션) + 대표이미지 1개
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
       where p.status = org.example.stamppaw_backend.market.entity.ProductStatus.SERVICE
         and (:pattern is null or lower(p.name) like :pattern)
       order by p.id desc
       """)
    Page<ProductListRow> findServiceListByName(@Param("pattern") String pattern, Pageable pageable);

    @Query("""
    select p.id as id,
           p.name as name,
           p.category as category,
           p.status as status,
           p.price as price,
           i.imageUrl as mainImageUrl
    from Product p
    left join ProductImage i on i.product = p and i.isMain = true
    where p.category = :category and p.status = :status
    order by p.id desc
    """)
    List<ProductListRow> findListByCategoryAndStatus(Category category, ProductStatus status);


}

