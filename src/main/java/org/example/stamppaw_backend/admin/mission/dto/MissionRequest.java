package org.example.stamppaw_backend.admin.mission.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.example.stamppaw_backend.admin.mission.dto.walk.WalkCompleteMissionRequest;
import org.example.stamppaw_backend.admin.mission.dto.walk.WalkDistanceMissionRequest;
import org.example.stamppaw_backend.admin.mission.dto.walk.WalkTimeMissionRequest;
import org.example.stamppaw_backend.user_mission.entity.MissionType;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = WalkCompleteMissionRequest.class, name = "WALK_COMPLETE"),
        @JsonSubTypes.Type(value = WalkDistanceMissionRequest.class, name = "WALK_DISTANCE"),
        @JsonSubTypes.Type(value = WalkTimeMissionRequest.class, name = "WALK_TIME")
})
public abstract class MissionRequest {
    private MissionType type;
    private String content;
    private int point;

    public abstract MissionType getType();
}
