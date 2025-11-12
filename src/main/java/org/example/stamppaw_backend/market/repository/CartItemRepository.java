package org.example.stamppaw_backend.market.repository;

import org.example.stamppaw_backend.market.entity.Cart;
import org.example.stamppaw_backend.market.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // 특정 장바구니에 담긴 모든 아이템 조회
    List<CartItem> findByCart(Cart cart);

    // 장바구니 + 상품 기준으로 중복 여부 확인
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);

    // 장바구니 아이템 삭제
    void deleteByCartIdAndProductId(Long cartId, Long productId);
}
