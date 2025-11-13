package org.example.stamppaw_backend.companion.service;

import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.common.exception.ErrorCode;
import org.example.stamppaw_backend.common.exception.StampPawException;
import org.example.stamppaw_backend.companion.dto.request.CompanionReviewCreateRequest;
import org.example.stamppaw_backend.companion.dto.response.CompanionReviewResponse;
import org.example.stamppaw_backend.companion.entity.*;
import org.example.stamppaw_backend.companion.repository.CompanionReviewMappingRepository;
import org.example.stamppaw_backend.companion.repository.CompanionReviewRepository;
import org.example.stamppaw_backend.user.entity.User;
import org.example.stamppaw_backend.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanionReviewService {
    private final CompanionReviewRepository companionReviewRepository;
    private final UserService userService;
    private final CompanionApplyService companionApplyService;
    private final CompanionReviewTagService companionReviewTagService;
    private final CompanionReviewMappingRepository mappingRepository;

    public void createReview(CompanionReviewCreateRequest request, Long applyId, Long userId) {
        User user = userService.getUserOrException(userId);
        CompanionApply companionApply = companionApplyService.getApplyOrException(applyId);
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
