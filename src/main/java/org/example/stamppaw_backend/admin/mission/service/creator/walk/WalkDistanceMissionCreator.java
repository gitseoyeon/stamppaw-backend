package org.example.stamppaw_backend.admin.mission.service.creator.walk;

import org.example.stamppaw_backend.admin.mission.dto.walk.WalkDistanceMissionRequest;
import org.example.stamppaw_backend.admin.mission.entity.Mission;
import org.example.stamppaw_backend.admin.mission.service.creator.MissionCreator;
import org.example.stamppaw_backend.user_mission.entity.MissionType;
import org.springframework.stereotype.Component;

@Component
public class WalkDistanceMissionCreator implements MissionCreator<WalkDistanceMissionRequest> {

    @Override
    public MissionType supports() {
        return MissionType.WALK_DISTANCE;
    }

    @Override
    public Mission create(WalkDistanceMissionRequest req) {
        return Mission.builder()
                .type(MissionType.WALK_DISTANCE)
                .content(req.getContent())
                .point(req.getPoint())
                .targetDistance(req.getTargetDistance())
                .build();
    }
}
