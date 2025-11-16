package org.example.stamppaw_backend.admin.mission.service.handler.walk;

import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.admin.mission.service.handler.MissionHandler;
import org.example.stamppaw_backend.user_mission.entity.MissionType;
import org.example.stamppaw_backend.user_mission.entity.UserMission;
import org.example.stamppaw_backend.walk.entity.Walk;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WalkDistanceHandler implements MissionHandler {

    @Override
    public boolean supports(MissionType type) {
        return type == MissionType.WALK_DISTANCE;
    }

    @Override
    public boolean checkCompletion(UserMission userMission, Object data) {
        Walk walk = (Walk) data;
        int require = userMission.getMission().getTargetDistance();
        return walk.getDistance() != null && walk.getDistance() >= require;
    }
}
