package org.example.stamppaw_backend.market.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.stamppaw_backend.common.exception.ErrorCode;
import org.example.stamppaw_backend.common.exception.StampPawException;
import org.example.stamppaw_backend.market.dto.request.OrderCreateRequest;
import org.example.stamppaw_backend.market.entity.*;
import org.example.stamppaw_backend.market.repository.CartItemRepository;
import org.example.stamppaw_backend.market.repository.CartRepository;
import org.example.stamppaw_backend.market.repository.OrderRepository;
import org.example.stamppaw_backend.user.entity.User;
import org.example.stamppaw_backend.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final UserService userService;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;


    @Transactional
    public Order createOrder(Long userId, OrderCreateRequest request) {

        Cart cart = cartRepository.findById(request.getCartId())
                .orElseThrow(() -> new StampPawException(ErrorCode.CART_NOT_FOUND));

        Long ownerId = cart.getUser().getId();
        if (!ownerId.equals(userId)) {
            throw new StampPawException(ErrorCode.UNAUTHORIZED_CART_ACCESS);
        }

        User user = userService.getUserOrException(cart.getUser().getId());

        //선택된 CartItem만 필터링
        List<CartItem> selectedItems = cart.getCartItems().stream()
                .filter(item -> request.getCartItemIds().contains(item.getId()))
                .toList();

        BigDecimal totalAmount = selectedItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = Order.builder()
                .user(user)
                .status(OrderStatus.ORDER)
                .totalAmount(totalAmount)
                .shippingName(request.getShippingName())
                .shippingAddress(request.getShippingAddress())
                .shippingMobile(request.getShippingMobile())
                .shippingStatus(ShippingStatus.READY)
                .shippingFee(BigDecimal.ZERO)
                .build();

        //CartItem → OrderItem
        List<OrderItem> orderItems = selectedItems.stream()
                .map(ci -> OrderItem.builder()
                        .product(ci.getProduct())
                        .optionSummary(ci.getOptionSummary())
                        .price(ci.getPrice())
                        .quantity(ci.getQuantity())
                        .subtotal(ci.getSubtotal())
                        .userImageUrl(ci.getUserImageUrl())
                        .order(order)
                        .build())
                .toList();

        order.setOrderItems(orderItems);

        orderRepository.save(order);

        //선택한 장바구니 항목 삭제
        cartItemRepository.deleteAll(selectedItems);

        return order;
    }

}
