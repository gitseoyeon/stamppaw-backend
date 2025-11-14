package org.example.stamppaw_backend.mission.dto;

import lombok.*;
import org.example.stamppaw_backend.mission.entity.UserMission;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMissionDto {
    private Long id;
    private Long userId;
    private Long missionId;
    private String missionContent;
    private int rewardPoint;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean status;

    public static UserMissionDto fromEntity(UserMission userMission) {
        return UserMissionDto.builder()
                .id(userMission.getId())
                .userId(userMission.getUser().getId())
                .missionId(userMission.getMission().getId())
                .missionContent(userMission.getMission().getContent())
                .rewardPoint(userMission.getMission().getPoint())
                .startDate(userMission.getStartDate())
                .endDate(userMission.getEndDate())
                .status(userMission.isStatus())
                .build();
    }
}
