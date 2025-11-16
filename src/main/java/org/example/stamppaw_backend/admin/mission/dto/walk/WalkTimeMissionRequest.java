package org.example.stamppaw_backend.admin.mission.dto.walk;

import lombok.Getter;
import lombok.Setter;
import org.example.stamppaw_backend.admin.mission.dto.MissionRequest;
import org.example.stamppaw_backend.user_mission.entity.MissionType;

@Getter
@Setter
public class WalkTimeMissionRequest extends MissionRequest {
    private String content;
    private int point;
    private int targetTime;

    @Override
    public MissionType getType() {
        return MissionType.WALK_TIME;
    }
}
