package org.example.stamppaw_backend.companion.service;

import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.common.exception.ErrorCode;
import org.example.stamppaw_backend.common.exception.StampPawException;
import org.example.stamppaw_backend.companion.dto.request.CompanionReviewCreateRequest;
import org.example.stamppaw_backend.companion.dto.response.CompanionReviewResponse;
import org.example.stamppaw_backend.companion.entity.*;
import org.example.stamppaw_backend.companion.repository.CompanionApplyRepository;
import org.example.stamppaw_backend.companion.repository.CompanionReviewMappingRepository;
import org.example.stamppaw_backend.companion.repository.CompanionReviewRepository;
import org.example.stamppaw_backend.user.entity.User;
import org.example.stamppaw_backend.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanionReviewService {
    private final CompanionReviewRepository companionReviewRepository;
    private final UserService userService;
    private final CompanionApplyRepository companionApplyRepository;
    private final CompanionReviewTagService companionReviewTagService;
    private final CompanionReviewMappingRepository mappingRepository;

    public void createReview(CompanionReviewCreateRequest request, Long applyId, Long userId) {
        User user = userService.getUserOrException(userId);
        CompanionApply companionApply = companionApplyRepository.findById(applyId)
                .orElseThrow(() -> new StampPawException(ErrorCode.APPLY_NOT_FOUND));
        verifyApplyUser(user, companionApply);

        CompanionReview review = CompanionReview.builder()
                .apply(companionApply)
                .build();
        companionReviewRepository.save(review);

        List<CompanionReviewTag> tags = companionReviewTagService.getTags(request.getTags());
        for(CompanionReviewTag tag : tags) {
            CompanionReviewMapping reviewMapping = CompanionReviewMapping.builder()
                    .review(review)
                    .tag(tag)
                    .build();
            mappingRepository.save(reviewMapping);
        }

        user.addPoint(1);

    }

    @Transactional(readOnly = true)
    public Page<CompanionReviewResponse> getReceivedReviews(Pageable pageable, Long userId) {
        User user = userService.getUserOrException(userId);
        Page<CompanionReview> reviews = companionReviewRepository.findCompanionReviewByUser(pageable, user);

        return reviews.map(CompanionReviewResponse::receivedFrom);
    }

    @Transactional(readOnly = true)
    public Page<CompanionReviewResponse> getSendReviews(Pageable pageable, Long userId) {
        User user = userService.getUserOrException(userId);
        Page<CompanionReview> reviews = companionReviewRepository.findCompanionReviewByApplyUser(pageable, user);

        return reviews.map(CompanionReviewResponse::sendFrom);
    }

    public boolean isExistReview(Long applyId) {
        return companionReviewRepository.existsByApply_Id(applyId);
    }


    private void verifyApplyUser(User user, CompanionApply companionApply) {
        if(!companionApply.getApplicant().equals(user)) {
            if(!companionApply.getCompanion().getUser().equals(user)) {
                throw new StampPawException(ErrorCode.NOT_APPLY_USER);
            }
        }
        if(!companionApply.getStatus().equals(ApplyStatus.ACCEPTED)) {
            throw new StampPawException(ErrorCode.NOT_ALLOW_APPLY);
        }
    }
}
