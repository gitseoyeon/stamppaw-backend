package org.example.stamppaw_backend.companion.controller;

import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.companion.dto.request.CompanionReviewCreateRequest;
import org.example.stamppaw_backend.companion.dto.response.CompanionReviewResponse;
import org.example.stamppaw_backend.companion.entity.CompanionReviewTag;
import org.example.stamppaw_backend.companion.service.CompanionReviewService;
import org.example.stamppaw_backend.companion.service.CompanionReviewTagService;
import org.example.stamppaw_backend.user.service.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companion/review")
@RequiredArgsConstructor
public class CompanionReviewController {
    private final CompanionReviewService companionReviewService;
    private final CompanionReviewTagService companionReviewTagService;

    @PostMapping("/{applyId}")
    public ResponseEntity<String> createReview(@PathVariable Long applyId,
                                               @RequestBody CompanionReviewCreateRequest request,
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        companionReviewService.createReview(request, applyId, userDetails.getUser().getId());
        return ResponseEntity.ok("리뷰 작성을 완료했습니다.");
    }

    @GetMapping("/receive")
    public ResponseEntity<Page<CompanionReviewResponse>> getReceivedReview(@RequestParam(defaultValue = "0") int page,
                                                                           @RequestParam(defaultValue = "5") int size,
                                                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(companionReviewService.getReceivedReviews(pageable, userDetails.getUser().getId()));
    }

    @GetMapping("/send")
    public ResponseEntity<Page<CompanionReviewResponse>> getSendReview(@RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "5") int size,
                                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(companionReviewService.getSendReviews(pageable, userDetails.getUser().getId()));
    }

    @GetMapping("/all-tags")
    public ResponseEntity<List<CompanionReviewTag>> getAllTags() {
        return ResponseEntity.ok(companionReviewTagService.getAllTags());
    }

}
