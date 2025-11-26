package org.example.stamppaw_backend.walk.controller;

import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.common.exception.ErrorCode;
import org.example.stamppaw_backend.common.exception.StampPawException;
import org.example.stamppaw_backend.user.entity.User;
import org.example.stamppaw_backend.user.service.CustomUserDetails;
import org.example.stamppaw_backend.walk.dto.request.*;
import org.example.stamppaw_backend.walk.dto.response.*;
import org.example.stamppaw_backend.walk.entity.Walk;
import org.example.stamppaw_backend.walk.service.WalkMapService;
import org.example.stamppaw_backend.walk.service.WalkService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;

@RestController
@RequestMapping("/api/walks")
@RequiredArgsConstructor
public class WalkController {

    private final WalkService walkService;
    private final WalkMapService walkMapService;

    @GetMapping("/search")
    public Page<WalkSearchResponse> search(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestParam(required = false) String memo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Long userId = currentUser.getUser().getId();
        Page<Walk> result = walkService.search(userId, memo, page, size);
        return result.map(WalkSearchResponse::fromEntity);
    }

    @GetMapping("/my")
    public Page<WalkResponse> getMyWalks(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            Pageable pageable
    ) {
        if (currentUser == null) {
            throw new StampPawException(ErrorCode.UNAUTHORIZED);
        }
        User user = currentUser.getUser();

        return walkService.getUserWalks(user, pageable);
    }

    // 추후 다른 사람의 산책 목록 보기 조건 확장
    @GetMapping("/user/{userId}")
    public Page<WalkResponse> getWalksByUser(
            @PathVariable Long userId,
            Pageable pageable
    ) {
        return walkService.getWalksByUser(userId, pageable);
    }

    @PostMapping("/start")
    public ResponseEntity<WalkStartResponse> startWalk(
            @RequestBody WalkStartRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        WalkStartResponse response = walkService.startWalk(request, currentUser.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{walkId}/point")
    public ResponseEntity<Void> addPoints(
            @PathVariable Long walkId,
            @RequestBody WalkPointRequest request
    ) {
        walkMapService.addPoint(walkId, request.getLat(), request.getLng(), request.getTimestamp());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{walkId}/end")
    public ResponseEntity<WalkEndResponse> endWalk(
            @PathVariable Long walkId,
            @RequestBody WalkEndRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        WalkEndResponse response = walkService.endWalk(walkId, request, currentUser.getUser());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{walkId}/record")
    public ResponseEntity<WalkResponse> recordWalk(
            @PathVariable Long walkId,
            @ModelAttribute WalkRecordRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        try {
            WalkResponse response = walkService.editWalk(walkId, request, currentUser.getUser());
            return ResponseEntity.ok(response);
        } catch (MultipartException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{walkId}")
    public ResponseEntity<WalkResponse> getWalkDetail(
            @PathVariable Long walkId,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        WalkResponse response = walkService.getWalkDetail(walkId, currentUser.getUser());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<WalkResponse>> getUserWalksPaged(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<WalkResponse> walks = walkService.getUserWalks(currentUser.getUser(), pageable);
        return ResponseEntity.ok(walks);
    }

    @DeleteMapping("/{walkId}")
    public ResponseEntity<Void> deleteWalk(
            @PathVariable Long walkId,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        walkService.deleteWalk(walkId, currentUser.getUser());
        return ResponseEntity.ok().build();
    }
}
