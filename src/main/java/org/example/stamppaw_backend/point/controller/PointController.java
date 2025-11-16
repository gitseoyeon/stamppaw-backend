package org.example.stamppaw_backend.point.controller;

import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.point.dto.PointRequest;
import org.example.stamppaw_backend.point.dto.PointResponse;
import org.example.stamppaw_backend.point.service.PointService;
import org.example.stamppaw_backend.user.service.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/points")
@RequiredArgsConstructor
public class PointController {

    private final PointService pointService;

    @PostMapping("/add")
    public ResponseEntity<PointResponse> addPoint(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestBody PointRequest request
    ) {
        Long userId = currentUser.getUser().getId();
        PointResponse response = pointService.addPoint(userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PointResponse>> getMyPoints(
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        Long userId = currentUser.getUser().getId();
        List<PointResponse> responses = pointService.getPoints(userId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/total")
    public ResponseEntity<Integer> getMyTotalPoints(
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        Long userId = currentUser.getUser().getId();
        int total = pointService.getTotalPointsForUser(userId);
        return ResponseEntity.ok(total);
    }

}
