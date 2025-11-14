package org.example.stamppaw_backend.mission.service.handler;

import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.mission.entity.MissionType;
import org.example.stamppaw_backend.mission.entity.UserMission;
import org.example.stamppaw_backend.walk.entity.Walk;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WalkCompleteHandler implements MissionHandler {

    @Override
    public boolean supports(MissionType type) {
        return type == MissionType.WALK_COMPLETE;
    }

    @Override
    public boolean checkCompletion(UserMission userMission, Walk walk) {
        return true; // 산책 종료 시 즉시 완료
    }
}
