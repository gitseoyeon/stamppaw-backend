package org.example.stamppaw_backend.market.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.stamppaw_backend.common.exception.ErrorCode;
import org.example.stamppaw_backend.common.exception.StampPawException;
import org.example.stamppaw_backend.market.dto.request.CartCreateRequest;
import org.example.stamppaw_backend.market.dto.request.CartUpdateRequest;
import org.example.stamppaw_backend.market.dto.response.CartResponse;
import org.example.stamppaw_backend.market.entity.Cart;
import org.example.stamppaw_backend.market.entity.CartItem;
import org.example.stamppaw_backend.market.entity.Product;
import org.example.stamppaw_backend.market.repository.CartItemRepository;
import org.example.stamppaw_backend.market.repository.CartRepository;
import org.example.stamppaw_backend.market.repository.ProductRepository;
import org.example.stamppaw_backend.user.entity.User;
import org.example.stamppaw_backend.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserService userService;

    //카트 조회후 없으면 생성
    @Transactional
    public CartResponse getUserCart(Long userId) {

        User user = userService.getUserOrException(userId);

        Cart cart = cartRepository.findByUserIdWithItems(userId)
                .orElseGet(() -> cartRepository.save(
                        Cart.builder().user(user).build()
                ));

        return CartResponse.fromEntity(cart);
    }

    @Transactional
    public Cart createCartWithItems(Long userId, CartCreateRequest request) {

        User user = userService.getUserOrException(userId);

        // 장바구니 조회 or 생성
        Cart cart = cartRepository.findByUserIdWithItems(userId) //findByUser(user)
                .orElseGet(() -> cartRepository.save(Cart.builder().user(user).build()));

        for (CartCreateRequest.ItemDto itemDto : request.getItems()) {

            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new StampPawException(ErrorCode.PRODUCT_NOT_FOUND));

            //동일한 상품이 있는지 옵션 Summary 까지 비교하여 체크
            CartItem existing = cart.getCartItems().stream()
                    .filter(ci -> ci.getProduct().getId().equals(itemDto.getProductId()) &&
                            ci.getOptionSummary().equals(itemDto.getOptionSummary()))
                    .findFirst()
                    .orElse(null);

            if (existing != null) {
                existing.setQuantity(existing.getQuantity() + itemDto.getQuantity());
                existing.setSubtotal(
                        existing.getPrice().multiply(BigDecimal.valueOf(existing.getQuantity()))
                );
                continue;
            }

            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .optionSummary(itemDto.getOptionSummary())
                    .quantity(itemDto.getQuantity())
                    .price(itemDto.getPrice())
                    .subtotal(itemDto.getPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity())))
                    .userImageUrl(itemDto.getUserImageUrl())
                    .build();

            cart.getCartItems().add(newItem);
        }

        cartRepository.save(cart);

        return cartRepository.findByUserIdWithItems(userId)
                .orElse(cart);
    }

    @Transactional
    public void updateItemQuantity(Long userId, CartUpdateRequest request) {

        CartItem cartItem = cartItemRepository.findById(request.getCartItemId())
                .orElseThrow(() -> new StampPawException(ErrorCode.CART_ITEM_NOT_FOUND));

        Long ownerId = cartItem.getCart().getUser().getId();
        if (!ownerId.equals(userId)) {
            throw new StampPawException(ErrorCode.UNAUTHORIZED_CART_ACCESS);
        }

        if (request.getQuantity() <= 0) {
            throw new StampPawException(ErrorCode.INVALID_QUANTITY);
        }

        cartItem.setQuantity(request.getQuantity());

        BigDecimal extraPrice = request.getExtraPrice() != null ? request.getExtraPrice() : BigDecimal.ZERO;

        //새 단가 계산 = 기본가 + 추가금
        BigDecimal basePrice = cartItem.getProduct().getPrice();
        BigDecimal newPrice = basePrice.add(extraPrice);
        cartItem.setPrice(newPrice);

        //subtotal = (기본가 + 추가금) × 수량
        cartItem.setSubtotal(newPrice.multiply(BigDecimal.valueOf(request.getQuantity())));

        //log.info("🛒 CartItem[{}] updated → qty={}, basePrice={}, extraPrice={}, subtotal={}",
        //        cartItem.getId(), request.getQuantity(), basePrice, extraPrice, cartItem.getSubtotal());
    }

    @Transactional
    public void removeItem(Long userId, Long cartItemId) {

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new StampPawException(ErrorCode.CART_ITEM_NOT_FOUND));

        Long ownerId = item.getCart().getUser().getId();
        if (!ownerId.equals(userId)) {
            throw new StampPawException(ErrorCode.UNAUTHORIZED_CART_ACCESS);
        }

        Cart cart = item.getCart();
        cart.removeItem(item);
    }

}
