package org.example.stamppaw_backend.parttime.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.parttime.dto.request.PartTimeApplyStatusRequest;
import org.example.stamppaw_backend.parttime.dto.request.PartTimeCreateRequest;
import org.example.stamppaw_backend.parttime.dto.request.PartTimeRecruitmentStatusRequest;
import org.example.stamppaw_backend.parttime.dto.request.PartTimeUpdateRequest;
import org.example.stamppaw_backend.parttime.dto.response.PartTimeApplyResponse;
import org.example.stamppaw_backend.parttime.dto.response.PartTimeResponse;
import org.example.stamppaw_backend.parttime.dto.response.PartTimeUserApplyResponse;
import org.example.stamppaw_backend.parttime.service.PartTimeApplyService;
import org.example.stamppaw_backend.parttime.service.PartTimeService;
import org.example.stamppaw_backend.user.entity.User;
import org.example.stamppaw_backend.user.service.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parttime")
@RequiredArgsConstructor
public class PartTimeController {

    private final PartTimeService partTimeService;
    private final PartTimeApplyService partTimeApplyService;

    /** 파트타임 등록 */
    @PostMapping
    public PartTimeResponse createPartTime(@Valid PartTimeCreateRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        return partTimeService.createPartTime(request, userDetails.getUser().getId());
    }

    /** 전체 조회 */
    @GetMapping
    public ResponseEntity<Page<PartTimeResponse>> getAllPartTime(@RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(partTimeService.getAllPartTime(pageable));
    }

    /** 단일 조회 */
    @GetMapping("/{postId}")
    public PartTimeResponse getPartTime(@PathVariable Long postId) {
        return partTimeService.getPartTime(postId);
    }

    /** 내가 올린 파트타임 조회 */
    @GetMapping("/myPartTime")
    public ResponseEntity<Page<PartTimeResponse>> getUserPartTime(@RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(partTimeService.getUserPartTime(pageable, userDetails.getUser().getId()));
    }

    /** 수정 (multipart + patch) */
    @PatchMapping("/{postId}")
    public PartTimeResponse modifyPartTime(@PathVariable Long postId,
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @Valid PartTimeUpdateRequest request) {
        return partTimeService.modifyPartTime(postId, userDetails.getUser().getId(), request);
    }

    /** 삭제 */
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePartTime(@PathVariable Long postId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        partTimeService.deletePartTime(postId, userDetails.getUser().getId());
        return ResponseEntity.ok("삭제가 완료되었습니다.");
    }


    /** 신청하기 */
    @PostMapping("/{postId}/apply")
    public ResponseEntity<String> applyPartTime(@PathVariable Long postId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        partTimeService.applyPartTime(postId, userDetails.getUser().getId());
        return ResponseEntity.ok("신청이 완료되었습니다.");
    }

    /** 신청자 관리 조회 */
    @GetMapping("/{postId}/apply/manage")
    public ResponseEntity<Page<PartTimeApplyResponse>> getManagePartTime(@RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @PathVariable Long postId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(partTimeService.getApplyByUser(postId, userDetails.getUser().getId(), pageable));
    }

    /** 신청 상태 변경 */
    @PutMapping("/{postId}/apply/status/{applyId}")
    public ResponseEntity<String> changeApplyStatus(@PathVariable Long postId,
        @PathVariable Long applyId,
        @RequestBody PartTimeApplyStatusRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        partTimeService.changeApplyStatus(postId, applyId, userDetails.getUser().getId(), request.getStatus());
        return ResponseEntity.ok("상태가 변경 되었습니다.");
    }

    /** 모집 상태 변경 */
    @PutMapping("/{postId}")
    public ResponseEntity<String> changeRecruitmentStatus(@PathVariable Long postId,
        @RequestBody PartTimeRecruitmentStatusRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        partTimeService.changeRecruitmentStatus(postId, userDetails.getUser().getId(), request.getStatus());
        return ResponseEntity.ok("상태가 변경 되었습니다.");
    }

    /** 내가 신청한 파트타임 조회 */
    @GetMapping("/myApply")
    public ResponseEntity<Page<PartTimeUserApplyResponse>> getUserApply(@RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(partTimeApplyService.getUserApply(pageable, userDetails.getUser().getId()));
    }

    @GetMapping("/apply/my")
    public ResponseEntity<Page<PartTimeUserApplyResponse>> getUserApply(
        @AuthenticationPrincipal User user,
        Pageable pageable
    ) {
        return ResponseEntity.ok(partTimeService.getUserApply(pageable, user.getId()));
    }

}
