package org.example.stamppaw_backend.random.controller;

import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.random.dto.LocationUpdateRequest;
import org.example.stamppaw_backend.random.dto.LocationUpdateResponse;
import org.example.stamppaw_backend.random.dto.RandomPointDto;
import org.example.stamppaw_backend.random.service.RandomPointRedisService;
import org.example.stamppaw_backend.random.service.WalkLocationService;
import org.example.stamppaw_backend.user.service.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/random")
@RequiredArgsConstructor
public class RandomPointController {

    private final WalkLocationService walkLocationService;
    private final RandomPointRedisService randomPointRedisService;

    @PostMapping("/location/update")
    public ResponseEntity<LocationUpdateResponse> updateLocation(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestBody LocationUpdateRequest request
    ) {
        Long userId = currentUser.getUser().getId();
        return ResponseEntity.ok(walkLocationService.handle(userId, request));
    }

    @GetMapping("/points")
    public ResponseEntity<List<RandomPointDto>> getRandomPoints(
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        Long userId = currentUser.getUser().getId();
        List<RandomPointDto> points = randomPointRedisService.getPoints(userId);

        return ResponseEntity.ok(points);
    }
}
