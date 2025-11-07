package org.example.stamppaw_backend.market.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.stamppaw_backend.admin.market.dto.request.ProductCreateRequest;
import org.example.stamppaw_backend.common.S3Service;
import org.example.stamppaw_backend.common.exception.ErrorCode;
import org.example.stamppaw_backend.common.exception.StampPawException;
import org.example.stamppaw_backend.market.dto.response.ProductDetailResponse;
import org.example.stamppaw_backend.market.dto.response.ProductListResponse;
import org.example.stamppaw_backend.market.entity.*;
import org.example.stamppaw_backend.market.repository.ProductRepository;
import org.example.stamppaw_backend.market.repository.projection.ProductListRow;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final S3Service s3Service;

    @Transactional
    public Long createProduct(ProductCreateRequest req, List<MultipartFile> imageFiles)
    {
        //로그인, 관리자 권한 체크

        Product p = new Product();
        p.setCategory(req.getCategory());
        p.setName(req.getName());
        p.setDescription(req.getDescription());
        p.setPrice(req.getPrice());
        p.setStatus(req.getStatus() != null ? req.getStatus() : ProductStatus.READY);

        if (imageFiles == null || imageFiles.isEmpty()) {
            log.info("[ADMIN MARKET PRODUCT] 이미지 파일이 전달되지 않았습니다.");
        } else {

            boolean hasMain = false;
            for (int i = 0; i < imageFiles.size(); i++) {
                MultipartFile file = imageFiles.get(i);

                if (file == null || file.isEmpty()) continue;

                String imageUrl = s3Service.uploadFileAndGetUrl(file);

                ProductImage img = new ProductImage();
                img.setImageUrl(imageUrl);

                boolean isMain = (!hasMain && i == 0);
                img.setMain(isMain);
                if (isMain) hasMain = true;

                img.setSort(i);
                p.addImage(img);

            }
        }


        // 상품 옵션
        if (req.getOptions() != null && !req.getOptions().isEmpty()) {
            for (var dto : req.getOptions()) {

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

    public Page<ProductListResponse> getListForAdmin(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        return productRepository.findAllForAdmin(pageable)
                .map(ProductListResponse::fromRow);
    }

    // 관리자용 상품 검색 : 상태 필터 없이 전체 조회 + 검색 가능
    public Page<ProductListRow> getProductSearchForAdmin(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        String keyword = (name == null || name.isBlank()) ? null : name;

        return productRepository.findList(keyword, pageable);
    }

    // 프런트용 상품 검색 :  status = SERVICE 인 상품만 대상 + 검색 가능
    public Page<ProductListRow> getProductSearch(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        String pattern = (name == null || name.isBlank())
                ? null
                : "%" + name.toLowerCase(Locale.ROOT) + "%";

        return productRepository.findServiceListByName(pattern, pageable);
    }

    @Transactional(readOnly = true)
    public ProductDetailResponse getProductDetail(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new StampPawException(ErrorCode.PRODUCT_NOT_FOUND));

        return ProductDetailResponse.fromEntity(product);
    }

    public List<ProductListResponse> getProductsByCategory(Category category) {

        ProductStatus activeStatus = ProductStatus.SERVICE;

        return productRepository.findListByCategoryAndStatus(category, activeStatus)
                .stream()
                .map(ProductListResponse::fromRow)
                .toList();
    }

    @Transactional
    public void deleteProduct(Long productId) {
        productRepository.findById(productId)
                .orElseThrow(() -> new StampPawException(ErrorCode.PRODUCT_NOT_FOUND));


        productRepository.deleteById(productId);
    }

}

