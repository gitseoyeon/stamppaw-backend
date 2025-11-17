package org.example.stamppaw_backend.market.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.market.dto.request.OrderCreateRequest;
import org.example.stamppaw_backend.market.dto.response.OrderItemResponse;
import org.example.stamppaw_backend.market.dto.response.OrderListResponse;
import org.example.stamppaw_backend.market.dto.response.OrderResponse;
import org.example.stamppaw_backend.market.entity.Order;
import org.example.stamppaw_backend.market.entity.OrderStatus;
import org.example.stamppaw_backend.market.service.OrderService;
import org.example.stamppaw_backend.user.service.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid OrderCreateRequest request
    ) {
        Order order = orderService.createOrder(userDetails.getUser().getId(), request);
        return ResponseEntity.ok(OrderResponse.fromEntity(order));
    }

    @GetMapping
    public ResponseEntity<Page<OrderListResponse>> getUserOrders(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Pageable pageable
    ) {
        Page<OrderListResponse> orders =
                orderService.getUserOrders(userDetails.getUser().getId(), pageable);

        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}/items")
    public ResponseEntity<List<OrderItemResponse>> getOrderItems(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long orderId
    ) {

        List<OrderItemResponse> items = orderService.getOrderItemsByOrderId(userDetails.getUser().getId(), orderId);

        return ResponseEntity.ok(items);
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<Void> updateOrderStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long orderId,
            @RequestParam OrderStatus status
    ) {
        orderService.updateOrderStatus(userDetails.getUser().getId(), orderId, status);
        return ResponseEntity.ok().build();
    }
}
