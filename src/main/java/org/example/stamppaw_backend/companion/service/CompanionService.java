package org.example.stamppaw_backend.companion.service;

import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.common.S3Service;
import org.example.stamppaw_backend.common.exception.ErrorCode;
import org.example.stamppaw_backend.common.exception.StampPawException;
import org.example.stamppaw_backend.companion.dto.CompanionManageDto;
import org.example.stamppaw_backend.companion.dto.request.CompanionCreateRequest;
import org.example.stamppaw_backend.companion.dto.request.CompanionUpdateRequest;
import org.example.stamppaw_backend.companion.dto.response.CompanionApplyResponse;
import org.example.stamppaw_backend.companion.dto.response.CompanionResponse;
import org.example.stamppaw_backend.companion.entity.ApplyStatus;
import org.example.stamppaw_backend.companion.entity.Companion;
import org.example.stamppaw_backend.companion.entity.CompanionApply;
import org.example.stamppaw_backend.companion.entity.RecruitmentStatus;
import org.example.stamppaw_backend.companion.repository.CompanionRepository;
import org.example.stamppaw_backend.user.entity.User;
import org.example.stamppaw_backend.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanionService {
    private final CompanionRepository companionRepository;
    private final UserService userService;
    private final S3Service s3Service;
    private final CompanionApplyService companionApplyService;

    public CompanionResponse createCompanion(CompanionCreateRequest request, Long userId) {
        User user = userService.getUserOrException(userId);

        Companion companion = companionRepository.save(
                Companion.builder()
                        .title(request.getTitle())
                        .content(request.getContent())
                        .imageUrl(s3Service.uploadFileAndGetUrl(request.getImage()))
                        .user(user)
                        .build()
        );

        return CompanionResponse.fromEntity(companion);
    }

    @Transactional(readOnly = true)
    public Page<CompanionResponse> getAllCompanion(Pageable pageable) {
        Page<Companion> companions = companionRepository.findAll(pageable);
        return companions.map(CompanionResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public CompanionResponse getCompanion(Long postId) {
        Companion companion = getCompanionOrException(postId);

        return CompanionResponse.fromEntity(companion);
    }

    @Transactional(readOnly = true)
    public Page<CompanionResponse> getUserCompanion(Pageable pageable, Long userId) {
        User user = userService.getUserOrException(userId);
        Page<Companion> companions = companionRepository.findAllByUser(pageable, user);
        return companions.map(CompanionResponse::fromEntity);
    }

    public CompanionResponse modifyCompanion(Long postId, Long userId, CompanionUpdateRequest request) {
        User user = userService.getUserOrException(userId);
        Companion companion = getCompanionOrException(postId);
        if(!user.getId().equals(companion.getUser().getId())) {
            throw new StampPawException(ErrorCode.FORBIDDEN_ACCESS);
        }

        return companion.updateCompanion(
                CompanionManageDto.builder()
                        .title(request.getTitle())
                        .content(request.getContent())
                        .image(s3Service.uploadFileAndGetUrl(request.getImage()))
                        .build()
        );
    }

    public void deleteCompanion(Long postId, Long userId) {
        User user = userService.getUserOrException(userId);
        Companion companion = getCompanionOrException(postId);
        if(!user.getId().equals(companion.getUser().getId())) {
            throw new StampPawException(ErrorCode.FORBIDDEN_ACCESS);
        }

        companionRepository.delete(companion);
    }

    public void applyCompanion(Long postId, Long userId) {
        User user = userService.getUserOrException(userId);
        Companion companion = getCompanionOrException(postId);
        companionApplyService.saveCompanionApply(user, companion);
    }

    @Transactional(readOnly = true)
    public Page<CompanionApplyResponse> getApplyByUser(Long postId, Long userId, Pageable pageable) {
        User user = userService.getUserOrException(userId);
        Companion companion = getCompanionOrException(postId);
        verifyUser(user, companion);
        Page<CompanionApply> companionApplies = companionApplyService.getCompanionApply(companion, pageable);
        return companionApplies.map(CompanionApplyResponse::fromEntity);
    }

    public void changeApplyStatus(Long postId, Long applyId, Long userId, ApplyStatus status) {
        User user = userService.getUserOrException(userId);
        Companion companion = getCompanionOrException(postId);
        verifyUser(user, companion);
        companionApplyService.changeApplyStatus(applyId, status);
    }

    public void changeRecruitmentStatus(Long postId, Long userId, RecruitmentStatus status) {
        User user = userService.getUserOrException(userId);
        Companion companion = getCompanionOrException(postId);
        verifyUser(user, companion);
        companion.updateStatus(status);
    }

    private Companion getCompanionOrException(Long postId) {
        return companionRepository.findById(postId)
                .orElseThrow(() -> new StampPawException(ErrorCode.COMPANION_NOT_FOUND));
    }

    private void verifyUser (User user, Companion companion) {
        if(!companion.getUser().equals(user)) {
            throw new StampPawException(ErrorCode.FORBIDDEN_ACCESS);
        }
    }
}
