package org.example.stamppaw_backend.admin.mission.service.handler.walk;

import org.example.stamppaw_backend.admin.mission.service.handler.MissionHandler;
import org.example.stamppaw_backend.user_mission.entity.MissionType;
import org.example.stamppaw_backend.user_mission.entity.UserMission;
import org.example.stamppaw_backend.walk.entity.Walk;
import org.springframework.stereotype.Component;

@Component
public class WalkTimeHandler implements MissionHandler {

    @Override
    public boolean supports(MissionType type) {
        return type == MissionType.WALK_TIME;
    }

    @Override
    public boolean checkCompletion(UserMission userMission, Object data) {
        Walk walk = (Walk) data;
        int require = userMission.getMission().getTargetTime();
        return walk.getDuration() != null && walk.getDuration() >= require;
    }
}
