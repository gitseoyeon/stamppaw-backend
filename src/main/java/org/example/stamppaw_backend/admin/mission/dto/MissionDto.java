package org.example.stamppaw_backend.admin.mission.dto;

import lombok.*;
import org.example.stamppaw_backend.admin.mission.entity.Mission;
import org.example.stamppaw_backend.mission.entity.MissionType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MissionDto {
    private Long id;
    private String content;
    private int point;
    private MissionType type;

    public static MissionDto fromEntity(Mission mission) {
        return MissionDto.builder()
                .id(mission.getId())
                .content(mission.getContent())
                .point(mission.getPoint())
                .type(mission.getType())
                .build();
    }
}