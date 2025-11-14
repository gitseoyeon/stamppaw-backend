package org.example.stamppaw_backend.mission.service.handler;

import org.example.stamppaw_backend.mission.entity.MissionType;
import org.example.stamppaw_backend.mission.entity.UserMission;
import org.example.stamppaw_backend.walk.entity.Walk;

public interface MissionHandler {

    boolean supports(MissionType type);

    boolean checkCompletion(UserMission userMission, Walk walk);
}
