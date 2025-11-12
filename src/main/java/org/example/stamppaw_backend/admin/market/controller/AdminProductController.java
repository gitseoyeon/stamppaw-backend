package org.example.stamppaw_backend.admin.market.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.stamppaw_backend.admin.market.dto.request.ProductCreateRequest;
import org.example.stamppaw_backend.market.dto.response.ProductListResponse;
import org.example.stamppaw_backend.market.entity.Category;
import org.example.stamppaw_backend.market.entity.ProductStatus;
import org.example.stamppaw_backend.market.repository.projection.ProductListRow;
import org.example.stamppaw_backend.market.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/products")
@RequiredArgsConstructor
@Slf4j
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
                         @RequestParam(value = "imageFiles", required = false) List<MultipartFile> imageFiles,
                         @RequestParam(value = "mainImageFile", required = false) MultipartFile mainImageFile,
                         RedirectAttributes redirect)
    {

        if (binding.hasErrors()) {
            return "admin/market/product-form";
        }

        form.setMainImageFile(mainImageFile);

        Long id = productService.createProduct(form, imageFiles);

        redirect.addFlashAttribute("message", "상품이 등록되었습니다. (#" + id + ")");
        return "redirect:/admin/products";
    }

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
            Model model)
    {
        Page<ProductListResponse> products = productService.getListForAdmin(page, size);

        model.addAttribute("products", products);
        model.addAttribute("currentPage", products.getNumber());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("title", "상품 목록");

        return "admin/market/product-list";
    }

    @GetMapping("/search")
    public String listProductSearchForAdmin(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        Page<ProductListRow> products = productService.getProductSearchForAdmin(name, page, size);

        model.addAttribute("products", products);
        model.addAttribute("currentPage", products.getNumber());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("name", name); //검색 정보 유지용
        model.addAttribute("title", "상품 검색 결과");

        return "admin/market/product-list";
    }

    @GetMapping("/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.getProductDetail(id));
        return "admin/market/product-detail";
    }

    @PostMapping("/{id}/delete")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirect) {
        productService.deleteProduct(id);
        redirect.addFlashAttribute("message", "상품이 삭제되었습니다. (#" + id + ")");
        return "redirect:/admin/products";
    }
}
