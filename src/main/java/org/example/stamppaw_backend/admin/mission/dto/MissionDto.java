package org.example.stamppaw_backend.admin.mission.dto;

import lombok.*;
import org.example.stamppaw_backend.admin.mission.entity.Mission;
import org.example.stamppaw_backend.user_mission.entity.MissionType;

@Getter
@Setter
@Builder
public class MissionDto {
    private Long id;
    private MissionType type;
    private String content;
    private int point;
    private Integer targetDistance;
    private Integer targetTime;

    public static MissionDto fromEntity(Mission mission) {
        return MissionDto.builder()
                .id(mission.getId())
                .type(mission.getType())
                .content(mission.getContent())
                .point(mission.getPoint())
                .targetDistance(mission.getTargetDistance())
                .targetTime(mission.getTargetTime())
                .build();
    }
}
