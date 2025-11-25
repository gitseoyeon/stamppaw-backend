package org.example.stamppaw_backend.admin.market.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.stamppaw_backend.market.dto.response.OrderDetailResponse;
import org.example.stamppaw_backend.market.dto.response.OrderListResponse;
import org.example.stamppaw_backend.market.entity.OrderStatus;
import org.example.stamppaw_backend.market.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
@Slf4j
public class AdminOrderController {
    private final OrderService orderService;

    @GetMapping
    public String getOrdersPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) OrderStatus status,
            Model model
    ) {
        Page<OrderListResponse> orders = orderService.getAllOrderSummaries(status, page, size);

        model.addAttribute("orders", orders);
        model.addAttribute("selectedStatus", status);

        return "admin/market/order-list";
    }

    @GetMapping("/{orderId}")
    public String getOrderDetail(
            @PathVariable Long orderId,
            Model model
    ) {
        OrderDetailResponse detail = orderService.getOrderDetailForAdmin(orderId);
        model.addAttribute("detail", detail);

        return "admin/market/order-detail";
    }

    @PostMapping("/{orderId}/delete")
    public String deleteOrder(@PathVariable Long orderId) {

        orderService.deleteOrderForAdmin(orderId);

        return "redirect:admin/market/orders";
    }
}
