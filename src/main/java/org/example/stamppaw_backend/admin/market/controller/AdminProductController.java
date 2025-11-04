package org.example.stamppaw_backend.admin.market.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.admin.market.dto.request.ProductCreateRequest;
import org.example.stamppaw_backend.market.entity.Category;
import org.example.stamppaw_backend.market.entity.Product;
import org.example.stamppaw_backend.market.entity.ProductStatus;
import org.example.stamppaw_backend.market.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class AdminProductController {
    private final ProductService productService;

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("form", new ProductCreateRequest());
        return "admin/market/product-form";
    }


    @PostMapping
    public String create(@Valid @ModelAttribute("form") ProductCreateRequest form,
                         BindingResult binding,
                         RedirectAttributes redirect) {

        if (binding.hasErrors()) {
            return "admin/market/product-form";
        }

        Long id = productService.createProduct(form);

        redirect.addFlashAttribute("message", "상품이 등록되었습니다. (#" + id + ")");
        return "redirect:/admin/products";
    }

    /** 폼에 공통으로 내려줄 enum 목록 */
    @ModelAttribute("categories")
    public Category[] categories() {
        return Category.values();
    }

    @ModelAttribute("statuses")
    public ProductStatus[] statuses() {
        return ProductStatus.values();
    }

    @GetMapping
    public String listProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        Page<Product> products = productService.getProducts(page, size);

        model.addAttribute("products", products.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("title", "상품 목록");

        return "admin/market/product-list";
    }
}
