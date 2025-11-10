package org.example.stamppaw_backend.walk.dto.response;

import lombok.Builder;
import lombok.Data;
import org.example.stamppaw_backend.walk.entity.WalkPoint;

import java.time.LocalDateTime;

@Data
@Builder
public class WalkPointResponse {
    private Double lat;
    private Double lng;
    private LocalDateTime timestamp;

    public static WalkPointResponse fromEntity(WalkPoint walkPoint) {
        return WalkPointResponse.builder()
                .lat(walkPoint.getLat())
                .lng(walkPoint.getLng())
                .timestamp(walkPoint.getTimestamp())
                .build();
    }
}