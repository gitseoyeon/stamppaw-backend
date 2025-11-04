package org.example.stamppaw_backend.market.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.stamppaw_backend.admin.market.dto.request.ProductCreateRequest;
import org.example.stamppaw_backend.market.entity.Product;
import org.example.stamppaw_backend.market.entity.ProductImage;
import org.example.stamppaw_backend.market.entity.ProductOption;
import org.example.stamppaw_backend.market.entity.ProductStatus;
import org.example.stamppaw_backend.market.repository.ProductRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public Long createProduct(ProductCreateRequest req) {

        Product p = new Product();
        p.setCategory(req.getCategory());
        p.setName(req.getName());
        p.setDescription(req.getDescription());
        p.setPrice(req.getPrice());
        p.setStatus(req.getStatus() != null ? req.getStatus() : ProductStatus.READY);

        // 상품 이미지
        if (req.getImages() != null && !req.getImages().isEmpty()) {
            int idx = 0;
            boolean hasMain = false;
            for (var dto : req.getImages()) {
                // URL 없으면 스킵
                if (dto.getImageUrl() == null || dto.getImageUrl().isBlank()) continue;

                ProductImage img = new ProductImage();
                img.setImageUrl(dto.getImageUrl());

                boolean isMain = (dto.getIsMain() != null ? dto.getIsMain() : false);
                if (!hasMain && (isMain || idx == 0)) { //작업시 조건 확인
                    isMain = true;
                    hasMain = true;
                }
                img.setMain(isMain);
                img.setSort(dto.getSort() != null ? dto.getSort() : idx);
                p.addImage(img);
                idx++;
            }
        }

        // 상품 옵션
        if (req.getOptions() != null && !req.getOptions().isEmpty()) {
            for (var dto : req.getOptions()) {
                // 이름/값 모두 비어있으면 스킵
                if ((dto.getName() == null || dto.getName().isBlank()) &&
                        (dto.getValue() == null || dto.getValue().isBlank())) {
                    continue;
                }

                ProductOption opt = new ProductOption();
                opt.setName(dto.getName());
                opt.setValue(dto.getValue());
                opt.setExtraPrice(dto.getExtraPrice() != null ? dto.getExtraPrice() : BigDecimal.ZERO);
                p.addOption(opt);
            }
        }

        return productRepository.save(p).getId();
    }

    public Page<Product> getProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "registeredAt"));
        return productRepository.findAll(pageable);
    }
}

