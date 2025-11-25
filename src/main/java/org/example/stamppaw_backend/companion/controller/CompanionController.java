package org.example.stamppaw_backend.companion.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.companion.dto.request.CompanionApplyStatusRequest;
import org.example.stamppaw_backend.companion.dto.request.CompanionCreateRequest;
import org.example.stamppaw_backend.companion.dto.request.CompanionRecruitmentStatusRequest;
import org.example.stamppaw_backend.companion.dto.request.CompanionUpdateRequest;
import org.example.stamppaw_backend.companion.dto.response.CompanionApplyResponse;
import org.example.stamppaw_backend.companion.dto.response.CompanionResponse;
import org.example.stamppaw_backend.companion.dto.response.CompanionUserApplyResponse;
import org.example.stamppaw_backend.companion.service.CompanionApplyService;
import org.example.stamppaw_backend.companion.service.CompanionService;
import org.example.stamppaw_backend.user.entity.User;
import org.example.stamppaw_backend.user.service.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companion")
@RequiredArgsConstructor
public class CompanionController {
    private final CompanionService companionService;
    private final CompanionApplyService companionApplyService;

    @PostMapping
    public CompanionResponse createCompanion(@Valid CompanionCreateRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return companionService.createCompanion(request, userDetails.getUser().getId());
    }

    @GetMapping
    public ResponseEntity<Page<CompanionResponse>> getAllCompanion(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(companionService.getAllCompanion(pageable));
    }

    @GetMapping("/{postId}")
    public CompanionResponse getCompanion(@PathVariable Long postId) {
        return companionService.getCompanion(postId);
    }

    @GetMapping("/myCompanion")
    public ResponseEntity<Page<CompanionResponse>> getUserCompanions(@RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "5") int size,
                                                                     @AuthenticationPrincipal CustomUserDetails userDetails) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(companionService.getUserCompanion(pageable, userDetails.getUser().getId()));
    }

    @PatchMapping("/{postId}")
    public CompanionResponse modifyCompanion(@PathVariable Long postId,
                                             @AuthenticationPrincipal CustomUserDetails userDetails,
                                             @Valid CompanionUpdateRequest request) {
        return companionService.modifyCompanion(postId, userDetails.getUser().getId(), request);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deleteCompanion(@PathVariable Long postId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        companionService.deleteCompanion(postId, userDetails.getUser().getId());
        return ResponseEntity.ok("삭제가 완료되었습니다.");
    }

    @PostMapping("/{postId}/apply")
    public ResponseEntity<String> applyCompanion(@PathVariable Long postId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        companionService.applyCompanion(postId, userDetails.getUser().getId());
        return ResponseEntity.ok("신청이 완료되었습니다.");
    }

    @GetMapping("/{postId}/apply/manage")
    public ResponseEntity<Page<CompanionApplyResponse>> getManageCompanion(@RequestParam(defaultValue = "0") int page,
                                                                           @RequestParam(defaultValue = "5") int size,
                                                                           @PathVariable Long postId,
                                                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(companionService.getApplyByUser(postId, userDetails.getUser().getId(), pageable));
    }

    @PutMapping("/{postId}/apply/status/{applyId}")
    public ResponseEntity<String> changeApplyStatus(@PathVariable Long postId,
                                                    @PathVariable Long applyId,
                                                    @RequestBody CompanionApplyStatusRequest request,
                                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        companionService.changeApplyStatus(postId, applyId, userDetails.getUser().getId(), request.getStatus());
        return ResponseEntity.ok("상태가 변경 되었습니다.");
    }

    @PutMapping("/{postId}")
    public ResponseEntity<String> changeRecruitmentStatus(@PathVariable Long postId,
                                                    @RequestBody CompanionRecruitmentStatusRequest request,
                                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        companionService.changeRecruitmentStatus(postId, userDetails.getUser().getId(), request.getStatus());
        return ResponseEntity.ok("상태가 변경 되었습니다.");
    }

    @GetMapping("/myApply")
    public ResponseEntity<Page<CompanionUserApplyResponse>> getUserApply(@RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "5") int size,
                                                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(companionApplyService.getUserApply(pageable, userDetails.getUser().getId()));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<CompanionResponse>> searchCompanion(@RequestParam(value = "title", defaultValue = "") String title,
                                                   @RequestParam(value = "page", defaultValue = "0") int page,
                                                   @RequestParam(value = "size", defaultValue = "3") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(companionService.searchCompanions(pageable, title));
    }
}
