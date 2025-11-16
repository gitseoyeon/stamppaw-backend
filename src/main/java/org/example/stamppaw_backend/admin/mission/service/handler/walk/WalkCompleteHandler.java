package org.example.stamppaw_backend.admin.mission.service.handler.walk;

import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.admin.mission.service.handler.MissionHandler;
import org.example.stamppaw_backend.user_mission.entity.MissionType;
import org.example.stamppaw_backend.user_mission.entity.UserMission;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WalkCompleteHandler implements MissionHandler {

    @Override
    public boolean supports(MissionType type) {
        return type == MissionType.WALK_COMPLETE;
    }

    @Override
    public boolean checkCompletion(UserMission userMission, Object data) {
        return true; // 산책 종료 시 즉시 완료
    }
}
