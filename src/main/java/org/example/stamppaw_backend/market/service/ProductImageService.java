package org.example.stamppaw_backend.market.service;

import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.market.entity.ProductImage;
import org.example.stamppaw_backend.market.repository.ProductImageRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductImageService {
    private final ProductImageRepository imageRepository;

    // 엔티티 그대로 반환
    @Transactional(readOnly = true)
    public List<ProductImage> getLatestProductMainImages() {
        return imageRepository.findLatestServiceMainImages(PageRequest.of(0, 5));
    }

    // URL만 필요할 때 (레포에 해당 메서드가 있을 때)
    @Transactional(readOnly = true)
    public List<String> getLatestProductMainImageUrls() {
        return imageRepository.findLatestMainImageUrls(PageRequest.of(0, 5));
    }

}
