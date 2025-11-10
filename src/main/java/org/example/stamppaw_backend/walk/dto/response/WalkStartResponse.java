package org.example.stamppaw_backend.walk.dto.response;

import lombok.*;
import org.example.stamppaw_backend.walk.entity.Walk;
import org.example.stamppaw_backend.walk.entity.WalkStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalkStartResponse {
    private Long id;
    private Double startLat;
    private Double startLng;
    private LocalDateTime startTime;
    private String status;

    public static WalkStartResponse fromEntity(Walk walk) {
        return WalkStartResponse.builder()
                .id(walk.getId())
                .startLat(walk.getStartLat())
                .startLng(walk.getStartLng())
                .startTime(walk.getStartTime())
                .status(walk.getStatus() != null ? walk.getStatus().name() : WalkStatus.STARTED.name())
                .build();
    }
}
