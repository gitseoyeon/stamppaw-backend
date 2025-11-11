package org.example.stamppaw_backend.market.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.example.stamppaw_backend.common.BasicTimeEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "products",
        indexes = {
                @Index(name="idx_products_status", columnList="status")
        })
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BasicTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(length = 200, nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(precision = 18, scale = 2, nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    @Builder.Default
    private ProductStatus status = ProductStatus.READY;

    @Column(nullable = false)
    private String mainImageUrl;

    @ToString.Exclude
    @JsonManagedReference("product-images")
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductImage> images = new ArrayList<>();

    @ToString.Exclude
    @JsonManagedReference("product-options")
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<ProductOption> options = new LinkedHashSet<>();

    // 연관관계 편의 메서드
    public void addImage(ProductImage img){ img.setProduct(this); images.add(img); }
    public void addOption(ProductOption opt){
        opt.setProduct(this);
        options.add(opt);
    }

}

