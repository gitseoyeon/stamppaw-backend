package org.example.stamppaw_backend.market.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.market.dto.request.OrderCreateRequest;
import org.example.stamppaw_backend.market.dto.response.OrderListResponse;
import org.example.stamppaw_backend.market.dto.response.OrderResponse;
import org.example.stamppaw_backend.market.entity.Order;
import org.example.stamppaw_backend.market.entity.OrderStatus;
import org.example.stamppaw_backend.market.service.OrderService;
import org.example.stamppaw_backend.user.service.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createCart(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid OrderCreateRequest request
    ) {
        Order order = orderService.createOrder(userDetails.getUser().getId(), request);
        return ResponseEntity.ok(OrderResponse.fromEntity(order));
    }

    @GetMapping
    public Page<OrderListResponse> getUserOrders(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) OrderStatus status,
            @PageableDefault(size = 10, sort = "registeredAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Long userId = userDetails.getUser().getId();
        return orderService.getUserOrders(userId, status, pageable);
    }
}
