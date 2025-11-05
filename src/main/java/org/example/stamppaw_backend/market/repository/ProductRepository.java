package org.example.stamppaw_backend.market.repository;

import org.example.stamppaw_backend.market.entity.Product;
import org.example.stamppaw_backend.market.entity.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @EntityGraph(attributePaths = {"images", "options"})
    Optional<Product> findWithImagesAndOptionsById(Long id);

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @EntityGraph(attributePaths = {"images", "options"})
    Optional<Product> findByIdAndStatus(Long id, ProductStatus status);

    Page<Product> findByNameContainingIgnoreCaseAndStatus(
            String name,
            ProductStatus status,
            Pageable pageable
    );

    //관리자 목록
    @EntityGraph(attributePaths = {"images", "options"})
    Page<Product> findAll(Pageable pageable);

    //프런트목록 (status = SERVICE)
    @EntityGraph(attributePaths = {"images", "options"})
    Page<Product> findByStatus(ProductStatus status, Pageable pageable);
}

