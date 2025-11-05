package org.example.stamppaw_backend.market.controller;

import lombok.RequiredArgsConstructor;

import org.example.stamppaw_backend.market.repository.projection.ProductListRow;
import org.example.stamppaw_backend.market.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/market")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;



    @GetMapping("/products/search")
    public Page<ProductListRow> searchProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return productService.getProductSearchForFront(keyword, page, size);
    }


}
