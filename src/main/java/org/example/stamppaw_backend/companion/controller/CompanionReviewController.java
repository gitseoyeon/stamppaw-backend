package org.example.stamppaw_backend.companion.controller;

import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.companion.dto.request.CompanionReviewCreateRequest;
import org.example.stamppaw_backend.companion.service.CompanionReviewService;
import org.example.stamppaw_backend.user.service.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companion/review")
@RequiredArgsConstructor
public class CompanionReviewController {
    private final CompanionReviewService companionReviewService;

    @PostMapping("/{applyId}")
    public ResponseEntity<String> createReview(@PathVariable Long applyId,
                                               @RequestBody CompanionReviewCreateRequest request,
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        companionReviewService.createReview(request, applyId, userDetails.getUser().getId());
        return ResponseEntity.ok("리뷰 작성을 완료했습니다.");
    }
}
