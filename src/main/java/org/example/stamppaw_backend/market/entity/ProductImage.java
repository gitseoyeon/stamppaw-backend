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

    @Column(name="image_url", nullable = false)
    private String imageUrl;

    @Builder.Default
    private Integer sort = 1;

}
