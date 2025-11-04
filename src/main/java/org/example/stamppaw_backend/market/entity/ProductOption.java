package org.example.stamppaw_backend.market.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.stamppaw_backend.common.BasicTimeEntity;

import java.math.BigDecimal;

@Entity
@Table(name = "product_options",
        uniqueConstraints = {
                @UniqueConstraint(name="uk_product_option_name_value",
                        columnNames = {"product_id","name","value"})
        },
        indexes = {
                @Index(name="idx_product_options_product", columnList="product_id")
        })
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductOption extends BasicTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(length = 200, nullable = false)
    private String name;   // 예: SIZE, COLOR

    @Column(length = 200, nullable = false)
    private String value;  // 예: M, RED

    @Builder.Default
    @Column(name="extra_price", precision = 18, scale = 2, nullable = false)
    private BigDecimal extraPrice = BigDecimal.ZERO;

}

