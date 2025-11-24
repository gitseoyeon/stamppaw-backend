package org.example.stamppaw_backend.community.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.community.dto.request.CommentCreateRequest;
import org.example.stamppaw_backend.community.dto.request.CommentUpdateRequest;
import org.example.stamppaw_backend.community.dto.response.CommentResponse;
import org.example.stamppaw_backend.community.entity.Community;
import org.example.stamppaw_backend.community.service.CommentService;
import org.example.stamppaw_backend.community.service.CommunityService;
import org.example.stamppaw_backend.user.service.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;
    private final CommunityService communityService;

    @PostMapping
    public ResponseEntity<String> createComment(@Valid @RequestBody CommentCreateRequest request,
                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        Community community = communityService.getCommunityOrException(request.getCommunityId());
        commentService.createComment(community, request, userDetails.getUser().getId());
        return ResponseEntity.ok("댓글 생성 완료");
    }

    @GetMapping("/{communityId}")
    public ResponseEntity<Page<CommentResponse>> getComments(@PathVariable Long communityId,
                                                             @PageableDefault(size = 10, sort = "registeredAt", direction = Sort.Direction.DESC)
                                                             Pageable pageable) {
        Page<CommentResponse> response = commentService.getComments(communityId, pageable);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<String> updateComment(@PathVariable Long commentId,
                                                @Valid @RequestBody CommentUpdateRequest request,
                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        commentService.updateComment(commentId, request, userDetails.getUser().getId());
        return ResponseEntity.ok("댓글 수정 완료");
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        commentService.deleteComment(commentId, userDetails.getUser().getId());
        return ResponseEntity.ok("댓글 삭제 완료");
    }

}
