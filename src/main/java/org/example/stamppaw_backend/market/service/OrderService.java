package org.example.stamppaw_backend.market.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.stamppaw_backend.common.exception.ErrorCode;
import org.example.stamppaw_backend.common.exception.StampPawException;
import org.example.stamppaw_backend.market.dto.request.OrderCreateRequest;
import org.example.stamppaw_backend.market.dto.response.OrderItemResponse;
import org.example.stamppaw_backend.market.dto.response.OrderListResponse;
import org.example.stamppaw_backend.market.entity.*;
import org.example.stamppaw_backend.market.repository.CartItemRepository;
import org.example.stamppaw_backend.market.repository.CartRepository;
import org.example.stamppaw_backend.market.repository.OrderItemRepository;
import org.example.stamppaw_backend.market.repository.OrderRepository;
import org.example.stamppaw_backend.user.entity.User;
import org.example.stamppaw_backend.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final OrderItemRepository orderItemRepository;


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
                .shippingFee(request.getShippingFee())
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

        for (CartItem item : selectedItems) {
            cart.removeItem(item);
        }

        return order;
    }

    @Transactional(readOnly = true)
    public Page<OrderListResponse> getAllOrderSummaries(OrderStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        return orderRepository.findAllSummaries(status, pageable)
                .map(OrderListResponse::fromProjection);
    }

    @Transactional(readOnly = true)
    public Page<OrderListResponse> getUserOrders(Long userId, Pageable pageable) {
        return orderRepository.findAllByUserId(userId, pageable)
                .map(OrderListResponse::fromProjection);
    }

    public List<OrderItemResponse> getOrderItemsByOrderId(Long userId, Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new StampPawException(ErrorCode.ORDER_NOT_FOUND));

        Long ownerId = order.getUser().getId();
        if (!ownerId.equals(userId)) {
            throw new StampPawException(ErrorCode.UNAUTHORIZED_ORDER_ACCESS);
        }

        return orderItemRepository.findByOrderId(orderId)
                .stream()
                .map(OrderItemResponse::fromEntity)
                .toList();
    }


    @Transactional
    public void updateOrderStatus(Long userId, Long orderId, OrderStatus status) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new StampPawException(ErrorCode.ORDER_NOT_FOUND));

        Long ownerId = order.getUser().getId();
        if (!ownerId.equals(userId)) {
            throw new StampPawException(ErrorCode.UNAUTHORIZED_ORDER_ACCESS);
        }

        if (status == OrderStatus.CANCELED) {
            if (order.getStatus() != OrderStatus.ORDER) {
                throw new StampPawException(ErrorCode.ORDER_CANCEL_NOT_ALLOWED);
            }
        }

        orderRepository.updateOrderStatus(orderId, status);
    }
}
