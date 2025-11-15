package org.example.stamppaw_backend.user_mission.controller;

import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.user_mission.dto.UserMissionDto;
import org.example.stamppaw_backend.user_mission.service.UserMissionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-missions")
@RequiredArgsConstructor
public class UserMissionController {

    private final UserMissionService userMissionService;

    @PostMapping("/{id}/complete")
    public UserMissionDto completeMission(@PathVariable Long id) {
        return userMissionService.completeMission(id);
    }

    @GetMapping("/{userId}")
    public List<UserMissionDto> getUserMissions(@PathVariable Long userId) {
        return userMissionService.getUserMissions(userId);
    }
}