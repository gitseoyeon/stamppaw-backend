package org.example.stamppaw_backend.companion.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.companion.dto.request.CompanionCreateRequest;
import org.example.stamppaw_backend.companion.dto.request.CompanionUpdateRequest;
import org.example.stamppaw_backend.companion.dto.response.CompanionResponse;
import org.example.stamppaw_backend.companion.service.CompanionService;
import org.example.stamppaw_backend.user.entity.User;
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

    @PostMapping
    public CompanionResponse createCompanion(@Valid CompanionCreateRequest request, @AuthenticationPrincipal User user) {
        return companionService.createCompanion(request, user.getId());
    }

    @GetMapping
    public ResponseEntity<Page<CompanionResponse>> getAllCompanion(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(companionService.getAllCompanion(pageable));
    }

    @GetMapping("/{postId}")
    public CompanionResponse getCompanion(@PathVariable Long postId) {
        return companionService.getCompanion(postId);
    }

    @PatchMapping("/{postId}")
    public CompanionResponse modifyCompanion(@PathVariable Long postId,
                                             @AuthenticationPrincipal User user,
                                             @Valid CompanionUpdateRequest request) {
        return companionService.modifyCompanion(postId, user.getId(), request);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deleteCompanion(@PathVariable Long postId, @AuthenticationPrincipal User user) {
        companionService.deleteCompanion(postId, user.getId());
        return ResponseEntity.ok("삭제가 완료되었습니다.");
    }
}
