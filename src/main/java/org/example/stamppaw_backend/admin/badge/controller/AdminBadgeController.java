package org.example.stamppaw_backend.admin.badge.controller;

import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.admin.badge.dto.BadgeRequest;
import org.example.stamppaw_backend.admin.badge.service.AdminBadgeService;
import org.example.stamppaw_backend.badge.entity.Badge;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/badges")
public class AdminBadgeController {

    private final AdminBadgeService adminBadgeService;

    /**
     * 🔹 목록 + 생성 폼 + 수정 모드
     */
    @GetMapping
    public String listBadges(@RequestParam(required = false) Long editId, Model model) {

        List<Badge> badges = adminBadgeService.getAllBadges();

        model.addAttribute("badges", badges);
        model.addAttribute("editId", editId);

        BadgeRequest formRequest;

        if (editId != null) {
            Badge badge = adminBadgeService.getBadge(editId);
            formRequest = BadgeRequest.fromEntity(badge); // 수정 모드
        } else {
            formRequest = new BadgeRequest(); // 생성 모드
        }

        model.addAttribute("formRequest", formRequest);

        return "admin/badge/badges";
    }

    /**
     * 🔹 뱃지 생성
     */
    @PostMapping
    public String createBadge(@ModelAttribute BadgeRequest newRequest) {
        adminBadgeService.createBadge(newRequest);
        return "redirect:/admin/badges";
    }

    /**
     * 🔹 뱃지 수정
     */
    @PostMapping("/{id}")
    public String updateBadge(
            @PathVariable Long id,
            @ModelAttribute BadgeRequest updateRequest
    ) {
        adminBadgeService.updateBadge(id, updateRequest);
        return "redirect:/admin/badges?editId=" + id;
    }

    /**
     * 🔹 뱃지 삭제
     */
    @PostMapping("/{id}/delete")
    public String deleteBadge(@PathVariable Long id) {
        adminBadgeService.deleteBadge(id);
        return "redirect:/admin/badges";
    }
}
