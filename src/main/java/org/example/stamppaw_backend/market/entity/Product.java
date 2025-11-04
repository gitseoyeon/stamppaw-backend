package org.example.stamppaw_backend.market.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products",
        indexes = {
                @Index(name="idx_products_status", columnList="status"),
                @Index(name="idx_products_created_at", columnList="created_at")
        })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(length = 200, nullable = false)
    private String name;

    @Lob
    private String description;

    @Column(precision = 18, scale = 2, nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    @Builder.Default
    private ProductStatus status = ProductStatus.READY;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sort ASC, id ASC")
    private List<ProductImage> images = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductOption> options = new ArrayList<>();

    // 연관관계 편의 메서드
    public void addImage(ProductImage img){ img.setProduct(this); images.add(img); }
    public void addOption(ProductOption opt){ opt.setProduct(this); options.add(opt); }

    @PrePersist void prePersist(){
        if (status == null) status = ProductStatus.READY; // 기본값 보장(선택)
        createdAt = LocalDateTime.now();
    }
    @PreUpdate  void preUpdate(){ updatedAt = LocalDateTime.now(); }
}

