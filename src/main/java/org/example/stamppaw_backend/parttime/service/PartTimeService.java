package org.example.stamppaw_backend.parttime.service;

import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.common.S3Service;
import org.example.stamppaw_backend.common.exception.ErrorCode;
import org.example.stamppaw_backend.common.exception.StampPawException;
import org.example.stamppaw_backend.parttime.dto.PartTimeManageDto;
import org.example.stamppaw_backend.companion.entity.ApplyStatus;
import org.example.stamppaw_backend.companion.entity.RecruitmentStatus;
import org.example.stamppaw_backend.parttime.dto.request.PartTimeCreateRequest;
import org.example.stamppaw_backend.parttime.dto.request.PartTimeUpdateRequest;
import org.example.stamppaw_backend.parttime.dto.response.PartTimeApplyResponse;
import org.example.stamppaw_backend.parttime.dto.response.PartTimeResponse;
import org.example.stamppaw_backend.parttime.dto.response.PartTimeUserApplyResponse;
import org.example.stamppaw_backend.parttime.entity.PartTime;
import org.example.stamppaw_backend.parttime.entity.PartTimeApply;
import org.example.stamppaw_backend.parttime.repository.PartTimeApplyRepository;
import org.example.stamppaw_backend.parttime.repository.PartTimeRepository;
import org.example.stamppaw_backend.user.entity.User;
import org.example.stamppaw_backend.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PartTimeService {
    private final PartTimeRepository partTimeRepository;
    private final UserService userService;
    private final S3Service s3Service;
    private final PartTimeApplyService partTimeApplyService;
    private final PartTimeApplyRepository partTimeApplyRepository;


    public PartTimeResponse createPartTime(PartTimeCreateRequest request, Long userId) {
        User user = userService.getUserOrException(userId);

        PartTime partTime = partTimeRepository.save(
            PartTime.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .imageUrl(request.getImage() != null ? s3Service.uploadFileAndGetUrl(request.getImage()) : null)
                .user(user)
                .build()
        );

        return PartTimeResponse.fromEntity(partTime);
    }

    @Transactional(readOnly = true)
    public Page<PartTimeResponse> getAllPartTime(Pageable pageable) {
        Page<PartTime> partTimes = partTimeRepository.findAllOrderByregisteredAt(pageable);
        return partTimes.map(PartTimeResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public PartTimeResponse getPartTime(Long postId) {
        PartTime partTime = getPartTimeOrException(postId);

        return PartTimeResponse.fromEntity(partTime);
    }

    @Transactional(readOnly = true)
    public Page<PartTimeResponse> getUserPartTime(Pageable pageable, Long userId) {
        User user = userService.getUserOrException(userId);
        Page<PartTime> partTimes = partTimeRepository.findAllByUser(pageable, user);
        return partTimes.map(PartTimeResponse::fromEntity);
    }

    public PartTimeResponse modifyPartTime(Long postId, Long userId, PartTimeUpdateRequest request) {
        User user = userService.getUserOrException(userId);
        PartTime partTime = getPartTimeOrException(postId);
        if(!user.getId().equals(partTime.getUser().getId())) {
            throw new StampPawException(ErrorCode.FORBIDDEN_ACCESS);
        }

        String newImageUrl;

        // 1) 삭제 요청
        if (Boolean.TRUE.equals(request.getDeleteImage())) {
            if (partTime.getImageUrl() != null) {
                s3Service.deleteFile(partTime.getImageUrl());
            }
            newImageUrl = null;
        }
        // 2) 새 이미지 업로드
        else if (request.getImage() != null && !request.getImage().isEmpty()) {
            if (partTime.getImageUrl() != null) {
                s3Service.deleteFile(partTime.getImageUrl());
            }
            newImageUrl = s3Service.uploadFileAndGetUrl(request.getImage());
        }
        // 3) 기존 이미지 유지
        else {
            newImageUrl = partTime.getImageUrl();
        }

        return partTime.updatePartTime(
            PartTimeManageDto.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .image(newImageUrl)
                .build()
        );
    }

    public void deletePartTime(Long postId, Long userId) {
        User user = userService.getUserOrException(userId);
        PartTime partTime = getPartTimeOrException(postId);
        if(!user.getId().equals(partTime.getUser().getId())) {
            throw new StampPawException(ErrorCode.FORBIDDEN_ACCESS);
        }

        partTimeRepository.delete(partTime);
    }

    public void applyPartTime(Long postId, Long userId) {
        User user = userService.getUserOrException(userId);
        PartTime partTime = getPartTimeOrException(postId);
        partTimeApplyService.savePartTimeApply(user, partTime);
    }

    @Transactional(readOnly = true)
    public Page<PartTimeApplyResponse> getApplyByUser(Long postId, Long userId, Pageable pageable) {
        User user = userService.getUserOrException(userId);
        PartTime partTime = getPartTimeOrException(postId);
        verifyUser(user, partTime);
        Page<PartTimeApply> partTimeApplies = partTimeApplyService.getPartTimeApply(partTime, pageable);
        return partTimeApplies.map(PartTimeApplyResponse::fromEntity);
    }

    public void changeApplyStatus(Long postId, Long applyId, Long userId, ApplyStatus status) {
        User user = userService.getUserOrException(userId);
        PartTime partTime = getPartTimeOrException(postId);
        verifyUser(user, partTime);
        partTimeApplyService.changeApplyStatus(applyId, status);
    }

    public void changeRecruitmentStatus(Long postId, Long userId, RecruitmentStatus status) {
        User user = userService.getUserOrException(userId);
        PartTime partTime = getPartTimeOrException(postId);
        verifyUser(user, partTime);
        partTime.updateStatus(status);
    }

    private PartTime getPartTimeOrException(Long postId) {
        return partTimeRepository.findById(postId)
            .orElseThrow(() -> new StampPawException(ErrorCode.PARTTIME_NOT_FOUND));
    }

    private void verifyUser (User user, PartTime partTime) {
        if(!partTime.getUser().equals(user)) {
            throw new StampPawException(ErrorCode.FORBIDDEN_ACCESS);
        }
    }

    @Transactional(readOnly = true)
    public Page<PartTimeUserApplyResponse> getUserApply(Pageable pageable, Long userId) {
        User user = userService.getUserOrException(userId);
        Page<PartTimeApply> applies = partTimeApplyRepository.findByApplicant(user, pageable);
        return applies.map(PartTimeUserApplyResponse::from);
    }

    @Transactional(readOnly = true)
    public Page<PartTimeResponse> searchPartTimes(Pageable pageable, String title) {
        Page<PartTime> results = partTimeRepository.findByTitle(pageable, title);
        return results.map(PartTimeResponse::fromEntity);
    }



}
