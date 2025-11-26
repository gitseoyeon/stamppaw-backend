package org.example.stamppaw_backend.community.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.community.dto.request.CommunityCreateRequest;
import org.example.stamppaw_backend.community.dto.request.CommunityModifyRequest;
import org.example.stamppaw_backend.community.dto.response.CommunityResponse;
import org.example.stamppaw_backend.community.service.CommunityService;
import org.example.stamppaw_backend.community.service.LikeService;
import org.example.stamppaw_backend.user.service.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/community")
public class CommunityController {
    private final CommunityService communityService;
    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<String> createCommunity(@Valid CommunityCreateRequest request,
                                                  @AuthenticationPrincipal CustomUserDetails userDetails) {
        communityService.createCommunity(request, userDetails.getUser().getId());
        return ResponseEntity.ok("글 작성을 완료했습니다.");
    }

    @GetMapping
    public ResponseEntity<Page<CommunityResponse>> getCommunities(@RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(communityService.getCommunities(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommunityResponse> getCommunity (@PathVariable Long id, HttpServletRequest request,
                                                           @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long userId = (userDetails != null) ? userDetails.getUser().getId() : null;
        return ResponseEntity.ok(
                communityService.getCommunity(id, request, userId)
        );
    }

    @GetMapping("/user")
    public ResponseEntity<Page<CommunityResponse>> getUserCommunity(@RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "5") int size,
                                                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(communityService.getUserCommunity(pageable, userDetails.getUser().getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> modifyCommunity(@PathVariable Long id,
                                                  @Valid CommunityModifyRequest request,
                                                  @AuthenticationPrincipal CustomUserDetails userDetails) {
        communityService.modifyCommunity(id, request, userDetails.getUser().getId());
        return ResponseEntity.ok("수정이 완료되었습니다.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCommunity(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        communityService.deleteCommunity(id, userDetails.getUser().getId());
        return ResponseEntity.ok("삭제가 완료 되었습니다.");
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<?> toggleLike(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        boolean isLiked = communityService.toggleLike(id, userDetails.getUser().getId());
        Long likeCount = likeService.getLikeCount(id);

        return ResponseEntity.ok().body(Map.of(
                "isLiked", isLiked,
                "likeCount", likeCount
        ));
    }
}
