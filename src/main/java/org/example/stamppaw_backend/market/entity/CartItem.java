package org.example.stamppaw_backend.market.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.example.stamppaw_backend.common.BasicTimeEntity;


import java.math.BigDecimal;

@Entity
@Table(name = "cart_items",
        indexes = @Index(name="idx_cart_items_cart", columnList="cart_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"cart", "product"})
@EqualsAndHashCode(callSuper = false, exclude = {"cart", "product"})
public class CartItem extends BasicTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference("cart-items")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "option_summary")
    private String optionSummary;

    @Column(precision = 18, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer quantity;

    @Column(precision = 18, scale = 2, nullable = false)
    private BigDecimal subtotal;

    @Column(name = "user_image_url", length = 2048)
    private String userImageUrl;
}
