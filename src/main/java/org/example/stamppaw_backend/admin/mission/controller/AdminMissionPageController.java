package org.example.stamppaw_backend.admin.mission.controller;

import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.admin.mission.dto.MissionDto;
import org.example.stamppaw_backend.admin.mission.service.MissionService;
import org.example.stamppaw_backend.user_mission.entity.MissionType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/missions")
@RequiredArgsConstructor
//@PreAuthorize("hasRole('ADMIN')")
public class AdminMissionPageController {

    private final MissionService missionService;

    @GetMapping
    public String missionsListPage(Model model) {
        List<MissionDto> missions = missionService.getAllMissions();
        model.addAttribute("missions", missions);
        return "admin/missions/mission-list";
    }

    @GetMapping("/create")
    public String missionCreatePage(Model model) {
        model.addAttribute("missionTypes", MissionType.values());

        List<MissionType> usedTypes = missionService.getUsedMissionTypes();
        model.addAttribute("usedTypes", usedTypes != null ? usedTypes : List.of());

        return "admin/missions/mission-create";
    }

    @GetMapping("/{id}/edit")
    public String missionEditPage(@PathVariable Long id, Model model) {
        MissionDto mission = missionService.getMissionById(id);
        model.addAttribute("mission", mission);
        model.addAttribute("missionTypes", MissionType.values());
        return "admin/missions/mission-edit";
    }

}
