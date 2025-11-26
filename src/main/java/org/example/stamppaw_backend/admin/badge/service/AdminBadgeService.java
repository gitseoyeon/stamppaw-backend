package org.example.stamppaw_backend.admin.badge.service;

import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.admin.badge.dto.BadgeRequest;
import org.example.stamppaw_backend.badge.entity.Badge;
import org.example.stamppaw_backend.badge.repository.BadgeRepository;
import org.example.stamppaw_backend.common.S3Service;
import org.example.stamppaw_backend.common.exception.ErrorCode;
import org.example.stamppaw_backend.common.exception.StampPawException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminBadgeService {

    private final BadgeRepository badgeRepository;
    private final S3Service s3Service;

    // ==========================================================
    // 목록 조회
    // ==========================================================
    @Transactional(readOnly = true)
    public List<Badge> getAllBadges() {
        return badgeRepository.findAllByOrderByIdAsc();
    }

    // ==========================================================
    // 단일 조회
    // ==========================================================
    @Transactional(readOnly = true)
    public Badge getBadge(Long id) {
        return badgeRepository.findById(id)
                .orElseThrow(() -> new StampPawException(ErrorCode.BADGE_NOT_FOUND));
    }

    // ==========================================================
    // 생성
    // ==========================================================
    public Badge createBadge(BadgeRequest request) {

        // 아이콘 업로드 처리
        String iconUrl = null;

        if (request.getIcon() != null && !request.getIcon().isEmpty()) {
            iconUrl = s3Service.uploadFileAndGetUrl(request.getIcon());
        }

        // DTO → Entity 변환
        Badge badge = request.toEntity(iconUrl);

        return badgeRepository.save(badge);
    }

    // ==========================================================
    // 수정
    // ==========================================================
    public Badge updateBadge(Long id, BadgeRequest request) {

        Badge badge = badgeRepository.findById(id)
                .orElseThrow(() -> new StampPawException(ErrorCode.BADGE_NOT_FOUND));

        // 기존 아이콘
        String iconUrl = badge.getIconUrl();

        // 새 아이콘 업로드
        if (request.getIcon() != null && !request.getIcon().isEmpty()) {
            if (iconUrl != null) {
                s3Service.deleteFile(iconUrl);
            }
            iconUrl = s3Service.uploadFileAndGetUrl(request.getIcon());
        }

        // DTO → Entity 반영
        request.applyTo(badge, iconUrl);

        return badge;
    }

    // ==========================================================
    // 삭제
    // ==========================================================
    public void deleteBadge(Long id) {

        Badge badge = badgeRepository.findById(id)
                .orElseThrow(() -> new StampPawException(ErrorCode.BADGE_NOT_FOUND));

        // S3 아이콘 삭제
        if (badge.getIconUrl() != null) {
            s3Service.deleteFile(badge.getIconUrl());
        }

        badgeRepository.delete(badge);
    }
}
