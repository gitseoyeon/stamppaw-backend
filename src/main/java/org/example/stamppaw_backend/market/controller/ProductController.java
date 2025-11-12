package org.example.stamppaw_backend.market.controller;

import lombok.RequiredArgsConstructor;

import org.example.stamppaw_backend.market.dto.request.ProductSearchRequest;
import org.example.stamppaw_backend.market.dto.response.ProductDetailResponse;
import org.example.stamppaw_backend.market.dto.response.ProductListResponse;
import org.example.stamppaw_backend.market.entity.Category;
import org.example.stamppaw_backend.market.repository.projection.ProductListRow;
import org.example.stamppaw_backend.market.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/market")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping("/products/search")
    public Page<ProductListRow> searchProducts(@RequestBody ProductSearchRequest req) {
        return productService.getProductSearch(req.getKeyword(), req.getPage(), req.getSize());
    }

    @GetMapping("/products/{id}")
    public ProductDetailResponse getDetail(@PathVariable Long id) {
        return productService.getProductDetail(id);
    }


    @GetMapping("/products/latest")
    public List<ProductListResponse> getLatestMainImageUrls() {
        return productService.getLatestServiceProducts();
    }

    @GetMapping("/products/category")
    public List<ProductListResponse> listProductsByCategory(
            @RequestParam Category category
    ) {
        return productService.getProductsByCategory(category);
    }
}
