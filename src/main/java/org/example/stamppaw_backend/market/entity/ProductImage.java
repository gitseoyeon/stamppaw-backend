package org.example.stamppaw_backend.market.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_images",
        indexes = {
                @Index(name="idx_product_images_product", columnList="product_id"),
                @Index(name="idx_product_images_is_main", columnList="product_id,is_main")
        })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // 스토리지 키 or URL 하나로 통일 권장 (둘 다 필요하면 명확히 역할 분리)
    @Column(name="image_url", nullable = false)
    private String imageUrl;

    @Column(name="is_main", nullable = false)
    private boolean isMain;

    @Builder.Default
    private Integer sort = 0;

    @Column(name="created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist void prePersist(){ createdAt = LocalDateTime.now(); }
}
