package org.example.stamppaw_backend.admin.mission.service.creator;

import org.example.stamppaw_backend.admin.mission.dto.MissionRequest;
import org.example.stamppaw_backend.admin.mission.entity.Mission;
import org.example.stamppaw_backend.user_mission.entity.MissionType;

public interface MissionCreator<T extends MissionRequest> {
    MissionType supports();
    Mission create(T request);
    void update(Mission mission, T request);
}
