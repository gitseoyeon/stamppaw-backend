package org.example.stamppaw_backend.companion.service;

import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.common.S3Service;
import org.example.stamppaw_backend.common.exception.ErrorCode;
import org.example.stamppaw_backend.common.exception.StampPawException;
import org.example.stamppaw_backend.companion.dto.CompanionDto;
import org.example.stamppaw_backend.companion.dto.request.CompanionCreateRequest;
import org.example.stamppaw_backend.companion.dto.request.CompanionUpdateRequest;
import org.example.stamppaw_backend.companion.dto.response.CompanionResponse;
import org.example.stamppaw_backend.companion.entity.Companion;
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

    public CompanionResponse createCompanion(CompanionCreateRequest request, Long userId) {
        User user = userService.getUserOrExcepion(userId);

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

    public CompanionResponse modifyCompanion(Long postId, Long userId, CompanionUpdateRequest request) {
        User user = userService.getUserOrExcepion(userId);
        Companion companion = getCompanionOrException(postId);
        if(!user.getId().equals(companion.getUser().getId())) {
            throw new StampPawException(ErrorCode.FORBIDDEN_ACCESS);
        }

        return companion.updateCompanion(
                CompanionDto.builder()
                        .title(request.getTitle())
                        .content(request.getContent())
                        .image(s3Service.uploadFileAndGetUrl(request.getImage()))
                        .build()
        );
    }

    public void deleteCompanion(Long postId, Long userId) {
        User user = userService.getUserOrExcepion(userId);
        Companion companion = getCompanionOrException(postId);
        if(!user.getId().equals(companion.getUser().getId())) {
            throw new StampPawException(ErrorCode.FORBIDDEN_ACCESS);
        }

        companionRepository.delete(companion);
    }

    private Companion getCompanionOrException(Long postId) {
        return companionRepository.findById(postId)
                .orElseThrow(() -> new StampPawException(ErrorCode.COMPANION_NOT_FOUND));
    }
}
