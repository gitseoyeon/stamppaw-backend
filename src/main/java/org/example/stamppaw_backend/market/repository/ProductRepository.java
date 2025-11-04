package org.example.stamppaw_backend.market.repository;

import org.example.stamppaw_backend.market.entity.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @EntityGraph(attributePaths = {"images", "options"})
    Optional<Product> findWithImagesAndOptionsById(Long id);
}

