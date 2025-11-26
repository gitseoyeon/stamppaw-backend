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
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final S3Service s3Service;

    @Transactional
    public Long createProduct(ProductCreateRequest req,
                              List<MultipartFile> imageFiles)
    {
        //로그인, 관리자 권한 체크

        //대표이미지 - 필수
        MultipartFile mainFile = req.getMainImageFile();
        if (mainFile == null || mainFile.isEmpty()) {
            throw new IllegalArgumentException("대표 이미지는 필수입니다.");
        }

        String mainImageUrl = s3Service.uploadFileAndGetUrl(mainFile);

        Product p = new Product();
        p.setCategory(req.getCategory());
        p.setName(req.getName());
        p.setDescription(req.getDescription());
        p.setPrice(req.getPrice());
        p.setStatus(req.getStatus() != null ? req.getStatus() : ProductStatus.READY);
        p.setMainImageUrl(mainImageUrl);

        //추가이미지 3개 - 선택
        if (imageFiles == null || imageFiles.isEmpty()) {
            log.info("[ADMIN MARKET PRODUCT] 이미지 파일이 전달되지 않았습니다.");
        } else {

            for (int i = 0; i < imageFiles.size(); i++) {
                MultipartFile file = imageFiles.get(i);
                if (file == null || file.isEmpty()) continue;

                String imageUrl = s3Service.uploadFileAndGetUrl(file);

                ProductImage img = new ProductImage();
                img.setImageUrl(imageUrl);

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

                ProductOption opt = ProductOption.builder()
                        .name(dto.getName())
                        .value(dto.getValue())
                        .extraPrice(dto.getExtraPrice() != null ? dto.getExtraPrice() : BigDecimal.ZERO)
                        .build();

                p.addOption(opt);
            }
        }

        return productRepository.save(p).getId();
    }

    @Transactional(readOnly = true)
    public Page<ProductListResponse> getListForAdmin(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        return productRepository.findAllBy(pageable)
                .map(ProductListResponse::fromRow);
    }

    // 관리자용 상품 검색 : 상태 필터 없이 전체 조회 + 검색 가능
    @Transactional(readOnly = true)
    public Page<ProductListRow> getProductSearchForAdmin(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        // null 또는 공백이면 빈 문자열로 바꿔서 -> 전체 반환
        String keyword = (name == null || name.isBlank()) ? "" : name.trim();

        return productRepository.findByNameContainingIgnoreCase(keyword, pageable);
    }

    // 프런트용 상품 검색 :  status = SERVICE 인 상품만 대상 + 검색 가능
    @Transactional(readOnly = true)
    public Page<ProductListRow> getProductSearch(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        String keyword = (name == null || name.isBlank()) ? "" : name.trim();

        return productRepository.findByNameOrDescription(
                ProductStatus.SERVICE,
                keyword,
                pageable
        );
    }

    @Transactional(readOnly = true)
    public List<ProductListResponse> getLatestServiceProducts() {
        return productRepository.findTop5ByStatusOrderByIdDesc(ProductStatus.SERVICE)
                .stream()
                .map(ProductListResponse::fromRow)
                .toList();
    }

    @Transactional(readOnly = true)
    public Map<String, List<ProductListResponse>> getServiceProductsGrouped() {

        List<ProductListResponse> products = productRepository
                .findByStatus(ProductStatus.SERVICE)
                .stream()
                .map(ProductListResponse::fromEntity)
                .toList();

       // return products.stream().collect(Collectors.groupingBy(ProductListResponse::category));
        return products.stream()
                .collect(Collectors.groupingBy(ProductListResponse::category))
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().stream().limit(2).toList()   // ⭐ 여기서 최대 2개 제한
                ));
    }

    @Transactional(readOnly = true)
    public List<ProductListResponse> getProductsByCategory(Category category) {

        return productRepository.findByCategoryAndStatusOrderByIdDesc(category, ProductStatus.SERVICE)
                .stream()
                .map(ProductListResponse::fromRow)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductDetailResponse getProductDetail(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new StampPawException(ErrorCode.PRODUCT_NOT_FOUND));

        return ProductDetailResponse.fromEntity(product);
    }

    @Transactional
    public void deleteProduct(Long productId) {
        productRepository.findById(productId)
                .orElseThrow(() -> new StampPawException(ErrorCode.PRODUCT_NOT_FOUND));


        productRepository.deleteById(productId);
    }

}

