package org.example.stamppaw_backend.point.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.stamppaw_backend.mission.entity.MissionType;

@Getter
@AllArgsConstructor
public class PointResponse {
    private Long id;
    private int amount;
    private MissionType reason;
}