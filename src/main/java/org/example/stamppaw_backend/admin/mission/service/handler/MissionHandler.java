package org.example.stamppaw_backend.admin.mission.service.handler;

import org.example.stamppaw_backend.user_mission.entity.MissionType;
import org.example.stamppaw_backend.user_mission.entity.UserMission;

public interface MissionHandler {

    boolean supports(MissionType type);

    boolean checkCompletion(UserMission userMission, Object data);
}
