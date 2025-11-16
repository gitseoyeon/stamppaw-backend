package org.example.stamppaw_backend.admin.mission.service;

import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.admin.mission.entity.Mission;
import org.example.stamppaw_backend.admin.mission.repository.MissionRepository;
import org.example.stamppaw_backend.user_mission.entity.MissionType;
import org.example.stamppaw_backend.user_mission.repository.UserMissionRepository;
import org.example.stamppaw_backend.user.entity.User;
import org.example.stamppaw_backend.user.repository.UserRepository;
import org.example.stamppaw_backend.user_mission.service.UserMissionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MissionAssignScheduler {

    private final UserRepository userRepository;
    private final MissionRepository missionRepository;
    private final UserMissionRepository userMissionRepository;
    private final UserMissionService userMissionService;

    // 매일 00:00 실행
    @Scheduled(cron = "0 0 0 * * *")
    public void assignDailyMissions() {

        LocalDate today = LocalDate.now();

        List<User> users = userRepository.findAll();
        List<Mission> missions = missionRepository.findAll();

        for (User user : users) {

            for (Mission mission : missions) {

                MissionType type = mission.getType();
                Long userId = user.getId();
                Long missionId = mission.getId();

                boolean existsToday = userMissionRepository
                        .existsByUserIdAndMission_TypeAndStartDate(userId, type, today);
                if (existsToday) continue;

                boolean existsIncomplete = userMissionRepository
                        .existsByUserIdAndMission_TypeAndStatusFalse(userId, type);
                if (existsIncomplete) continue;

                userMissionService.createUserMission(userId, missionId);
            }
        }

        System.out.println("일일 미션 자동 할당 완료: " + today);
    }
}
