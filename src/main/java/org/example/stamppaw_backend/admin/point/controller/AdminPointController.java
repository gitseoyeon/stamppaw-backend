package org.example.stamppaw_backend.admin.point.controller;


import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.point.dto.PointRequest;
import org.example.stamppaw_backend.point.dto.PointResponse;
import org.example.stamppaw_backend.point.entity.Point;
import org.example.stamppaw_backend.point.service.PointService;
import org.example.stamppaw_backend.user.service.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/points")
@PreAuthorize("hasAuthority('USER')")
@RequiredArgsConstructor
public class AdminPointController {

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

    @GetMapping("/{userId}")
    public ResponseEntity<List<PointResponse>> getPointsByUserId(
            @PathVariable Long userId
    ) {
        List<PointResponse> points = pointService.getPoints(userId);
        return ResponseEntity.ok(points);
    }

    @GetMapping("/{userId}/total")
    public ResponseEntity<Integer> getTotalPointsByUserId(
            @PathVariable Long userId
    ) {
        int total = pointService.getTotalPointsForUser(userId);
        return ResponseEntity.ok(total);
    }

    @DeleteMapping("/{pointId}")
    public ResponseEntity<Void> deletePoint(@PathVariable Long pointId) {
        pointService.deletePoint(pointId);
        return ResponseEntity.noContent().build();
    }
}
