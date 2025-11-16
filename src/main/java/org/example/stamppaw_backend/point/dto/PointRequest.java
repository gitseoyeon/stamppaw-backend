package org.example.stamppaw_backend.point.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.stamppaw_backend.user_mission.entity.MissionType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PointRequest {
    private int amount;
    private MissionType reason;
}