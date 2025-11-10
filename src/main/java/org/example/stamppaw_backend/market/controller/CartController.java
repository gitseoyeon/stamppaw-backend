package org.example.stamppaw_backend.market.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.market.dto.request.CartCreateRequest;
import org.example.stamppaw_backend.market.dto.request.CartUpdateRequest;
import org.example.stamppaw_backend.market.dto.response.CartResponse;
import org.example.stamppaw_backend.market.entity.Cart;
import org.example.stamppaw_backend.market.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    // 유저 장바구니 조회
    @GetMapping("/{userId}")
    public ResponseEntity<CartResponse> getCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getUserCart(userId));
    }

    @PostMapping
    public ResponseEntity<CartResponse> createCart(@RequestBody @Valid CartCreateRequest request) {
        Cart cart = cartService.createCartWithItems(request);
        return ResponseEntity.ok(CartResponse.fromEntity(cart));
    }

    //카트 상품 수량 변경 => 옵션금액 변경
    @PatchMapping("/item/quantity")
    public ResponseEntity<Void> updateCartItemQuantity(@RequestBody CartUpdateRequest request) {
        cartService.updateItemQuantity(request);
        return ResponseEntity.ok().build();
    }

    //카트 상품 삭제
    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long cartItemId) {
        cartService.removeItem(cartItemId);
        return ResponseEntity.noContent().build();
    }
}
