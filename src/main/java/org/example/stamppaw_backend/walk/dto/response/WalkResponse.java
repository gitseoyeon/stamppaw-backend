package org.example.stamppaw_backend.walk.dto.response;

import lombok.*;
import org.example.stamppaw_backend.walk.entity.Walk;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WalkResponse {
    private Double distance;
    private Long duration;
    private Double startLat;
    private Double startLng;
    private Double endLat;
    private Double endLng;
    private String memo;
    private List<String> photoUrls;
    private String status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<WalkPointResponse> points;

    public static WalkResponse fromEntity(Walk walk) {
        return WalkResponse.builder()
                .distance(walk.getDistance())
                .duration(walk.getDuration())
                .startLat(walk.getStartLat())
                .startLng(walk.getStartLng())
                .endLat(walk.getEndLat())
                .endLng(walk.getEndLng())
                .memo(walk.getMemo())
                .status(walk.getStatus().name())
                .startTime(walk.getStartTime())
                .endTime(walk.getEndTime())
                .build();
    }
}