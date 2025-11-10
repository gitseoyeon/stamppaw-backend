package org.example.stamppaw_backend.walk.dto.request;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class WalkEndRequest {
    private Double lat;
    private Double lng;
    private LocalDateTime timestamp;
    private String memo;
}