package org.example.stamppaw_backend.market.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.stamppaw_backend.admin.market.dto.request.ProductCreateRequest;
import org.example.stamppaw_backend.market.dto.request.ProductSearchRequest;
import org.example.stamppaw_backend.market.dto.response.ProductListResponse;
import org.example.stamppaw_backend.market.entity.Product;
import org.example.stamppaw_backend.market.entity.ProductImage;
import org.example.stamppaw_backend.market.entity.ProductOption;
import org.example.stamppaw_backend.market.entity.ProductStatus;
import org.example.stamppaw_backend.market.repository.ProductRepository;
import org.example.stamppaw_backend.market.repository.projection.ProductListRow;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public Long createProduct(ProductCreateRequest req) {
        //로그인, 관리자 권한 체크

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

    public Page<ProductListResponse> getAdminList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        return productRepository.findAllForAdmin(pageable)
                .map(ProductListResponse::fromRow);
    }

    // 관리자용 상품 검색 : 상태 필터 없이 전체 조회 + 검색 가능
    public Page<ProductListRow> getProductSearchForAdmin(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        // 검색어 없으면 null 넣으면 자동 필터 제거
        String keyword = (name == null || name.isBlank()) ? null : name;

        return productRepository.findList(keyword, pageable);
    }

    // 프런트용 상품 검색 :  status = SERVICE 인 상품만 대상 + 검색 가능
    public Page<ProductListRow> getProductSearchForFront(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        String keyword = (name == null || name.isBlank()) ? null : name;

        return productRepository.findListByStatusAndName(
                ProductStatus.SERVICE,
                keyword,
                pageable
        );
    }

}

