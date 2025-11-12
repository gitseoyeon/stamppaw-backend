package org.example.stamppaw_backend.market.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.stamppaw_backend.market.dto.request.CartCreateRequest;
import org.example.stamppaw_backend.market.dto.request.CartUpdateRequest;
import org.example.stamppaw_backend.market.dto.response.CartResponse;
import org.example.stamppaw_backend.market.entity.Cart;
import org.example.stamppaw_backend.market.service.CartService;
import org.example.stamppaw_backend.user.service.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController {
    private final CartService cartService;

    // 유저 장바구니 조회
    @GetMapping
    public ResponseEntity<CartResponse> getCart(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(cartService.getUserCart(userDetails.getUser().getId()));
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
