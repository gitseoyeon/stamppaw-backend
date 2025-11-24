package org.example.stamppaw_backend.market.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.market.dto.request.OrderCreateRequest;
import org.example.stamppaw_backend.market.dto.response.*;
import org.example.stamppaw_backend.market.entity.Order;
import org.example.stamppaw_backend.market.entity.OrderStatus;
import org.example.stamppaw_backend.market.entity.ShippingStatus;
import org.example.stamppaw_backend.market.service.OrderService;
import org.example.stamppaw_backend.user.service.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
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
    public ResponseEntity<Page<OrderResponse>> getUserOrders(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Pageable pageable,
            @RequestParam(required = false, defaultValue = "ORDER") String orderStatus
    ) {
        Page<OrderResponse> orders =
                orderService.getUserOrders(userDetails.getUser().getId(), pageable, orderStatus);

        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}/detail")
    public ResponseEntity<OrderDetailResponse> getOrderDetail(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long orderId
    ) {
        Long userId = userDetails.getUser().getId();

        OrderDetailResponse response = orderService.getOrderDetail(userId, orderId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/shipping")
    public ResponseEntity<List<ShippingStatusResponse>> getAllStatuses() {

        List<ShippingStatusResponse> list = Arrays.stream(ShippingStatus.values())
                .map(st -> new ShippingStatusResponse(st.name(), st.getLabel()))
                .toList();

        return ResponseEntity.ok(list);
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
