package org.example.stamppaw_backend.badge.controller;

import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.badge.dto.UserBadgeResponse;
import org.example.stamppaw_backend.badge.service.UserBadgeService;
import org.example.stamppaw_backend.user.entity.User;
import org.example.stamppaw_backend.user.service.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/badges")
public class UserBadgeController {

    private final UserBadgeService userBadgeService;

    @GetMapping
    public List<UserBadgeResponse> getMyBadges(
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        return userBadgeService.getUserBadges(currentUser.getUser().getId());
    }

    @PostMapping("/representative/{badgeId}")
    public void setRepresentative(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long badgeId
    ) {
        userBadgeService.setRepresentative(currentUser.getUser(), badgeId);
    }

    @PostMapping("/representative/clear")
    public void clearRepresentative(
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        userBadgeService.clearRepresentative(currentUser.getUser());
    }

}
