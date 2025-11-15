package org.example.stamppaw_backend.admin.mission.service.creator.walk;

import org.example.stamppaw_backend.admin.mission.dto.walk.WalkCompleteMissionRequest;
import org.example.stamppaw_backend.admin.mission.entity.Mission;
import org.example.stamppaw_backend.admin.mission.service.creator.MissionCreator;
import org.example.stamppaw_backend.user_mission.entity.MissionType;
import org.springframework.stereotype.Component;

@Component
public class WalkCompleteMissionCreator implements MissionCreator<WalkCompleteMissionRequest> {

    @Override
    public MissionType supports() {
        return MissionType.WALK_COMPLETE;
    }

    @Override
    public Mission create(WalkCompleteMissionRequest req) {
        return Mission.builder()
                .type(MissionType.WALK_COMPLETE)
                .content(req.getContent())
                .point(req.getPoint())
                .build();
    }
}
