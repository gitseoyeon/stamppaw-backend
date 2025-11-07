package org.example.stamppaw_backend.market.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.example.stamppaw_backend.common.BasicTimeEntity;


@Entity
@Table(name = "product_images",
        indexes = {
                @Index(name="idx_product_images_product", columnList="product_id")
        })
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImage extends BasicTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference("product-images")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // 스토리지 키 or URL 하나로 통일 권장 (둘 다 필요하면 명확히 역할 분리)
    @Column(name="image_url", nullable = false)
    private String imageUrl;

    @Column(name="is_main", nullable = false)
    private boolean isMain;

    @Builder.Default
    private Integer sort = 1;

}
