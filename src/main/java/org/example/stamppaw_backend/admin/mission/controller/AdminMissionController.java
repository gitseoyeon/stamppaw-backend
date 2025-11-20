package org.example.stamppaw_backend.admin.mission.controller;

import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.admin.mission.dto.MissionRequest;
import org.example.stamppaw_backend.admin.mission.service.MissionAssignScheduler;
import org.example.stamppaw_backend.admin.mission.service.MissionService;
import org.example.stamppaw_backend.admin.mission.dto.MissionDto;
import org.example.stamppaw_backend.user_mission.dto.UserMissionDto;
import org.example.stamppaw_backend.user_mission.entity.UserMission;
import org.example.stamppaw_backend.user_mission.service.UserMissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/missions")
@RequiredArgsConstructor
//@PreAuthorize("hasAuthority('USER')")
public class AdminMissionController {

    private final MissionService missionService;
    private final UserMissionService userMissionService;
    private final MissionAssignScheduler missionAssignScheduler;

    @PostMapping
    public ResponseEntity<MissionDto> createMission(
            @RequestBody MissionRequest request
    ) {
        return ResponseEntity.ok(missionService.createMission(request));
    }

    @GetMapping
    public ResponseEntity<List<MissionDto>> getAllMissions() {
        return ResponseEntity.ok(missionService.getAllMissions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MissionDto> getMissionById(@PathVariable Long id) {
        return ResponseEntity.ok(missionService.getMissionById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MissionDto> updateMission(
            @PathVariable Long id,
            @RequestBody MissionRequest request
    ) {
        return ResponseEntity.ok(missionService.updateMission(id, request));
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteMission(@PathVariable Long id) {
//        missionService.deleteMission(id);
//        return ResponseEntity.noContent().build();
//    }

    @PostMapping("{userId}/assign/{missionId}")
    public ResponseEntity<UserMissionDto> assignMissionToUser(
            @PathVariable Long userId,
            @PathVariable Long missionId
    ) {
        UserMission userMission = userMissionService.createUserMission(userId, missionId);
        return ResponseEntity.ok(UserMissionDto.fromEntity(userMission));
    }

    @PostMapping("/assign-today")
    public ResponseEntity<String> assignTodayMissions() {
        missionAssignScheduler.assignDailyMissions();
        return ResponseEntity.ok().build();
    }

}
