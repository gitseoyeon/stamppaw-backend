package org.example.stamppaw_backend.user_mission.controller;

import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.user.service.CustomUserDetails;
import org.example.stamppaw_backend.user_mission.dto.UserMissionDto;
import org.example.stamppaw_backend.user_mission.service.UserMissionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-missions")
@RequiredArgsConstructor
public class UserMissionController {

    private final UserMissionService userMissionService;

    @GetMapping
    public List<UserMissionDto> getMyMissions(
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        Long userId = currentUser.getUser().getId();
        return userMissionService.getUserMissions(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/complete")
    public UserMissionDto completeMission(@PathVariable Long id){
        return userMissionService.completeMission(id);
    }

}