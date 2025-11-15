package org.example.stamppaw_backend.admin.mission.service.creator.walk;

import org.example.stamppaw_backend.admin.mission.dto.walk.WalkTimeMissionRequest;
import org.example.stamppaw_backend.admin.mission.entity.Mission;
import org.example.stamppaw_backend.admin.mission.service.creator.MissionCreator;
import org.example.stamppaw_backend.user_mission.entity.MissionType;
import org.springframework.stereotype.Component;

@Component
public class WalkTimeMissionCreator implements MissionCreator<WalkTimeMissionRequest> {

    @Override
    public MissionType supports() {
        return MissionType.WALK_TIME;
    }

    @Override
    public Mission create(WalkTimeMissionRequest req) {
        return Mission.builder()
                .type(MissionType.WALK_TIME)
                .content(req.getContent())
                .point(req.getPoint())
                .targetTime(req.getTargetTime())
                .build();
    }
}
