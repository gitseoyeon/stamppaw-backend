package org.example.stamppaw_backend.mission.service;

import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.admin.mission.entity.Mission;
import org.example.stamppaw_backend.mission.entity.MissionType;
import org.example.stamppaw_backend.mission.entity.UserMission;
import org.example.stamppaw_backend.mission.repository.UserMissionRepository;
import org.example.stamppaw_backend.point.entity.Point;
import org.example.stamppaw_backend.point.repository.PointRepository;
import org.example.stamppaw_backend.user.entity.User;
import org.example.stamppaw_backend.walk.entity.Walk;
import org.example.stamppaw_backend.mission.service.handler.MissionHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MissionProcessor {

    private final UserMissionRepository userMissionRepository;
    private final PointRepository pointRepository;
    private final List<MissionHandler> handlers;

    public void handleWalkCompleted(Walk walk) {

        Long userId = walk.getUser().getId();
        List<UserMission> missions = userMissionRepository.findByUserIdAndStatusFalseWithMission(userId);

        for (UserMission userMission : missions) {

            MissionType type = userMission.getMission().getType();

            handlers.stream()
                    .filter(h -> h.supports(type))
                    .findFirst()
                    .ifPresent(handler -> {

                        boolean isClear = handler.checkCompletion(userMission, walk);

                        if (isClear) {
                            completeMissionInternal(userMission);
                        }
                    });
        }
    }

    private void completeMissionInternal(UserMission userMission) {

        userMission.setStatus(true);
        userMissionRepository.save(userMission);

        Mission mission = userMission.getMission();
        User user = userMission.getUser();

        int point = mission.getPoint();

        user.addPoint(point);

        Point pointHistory = Point.builder()
                .user(user)
                .amount(point)
                .reason(mission.getType())
                .userMission(userMission)
                .build();

        pointRepository.save(pointHistory);
    }
}
