package org.example.stamppaw_backend.random.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RandomPointDto {
    private double lat;
    private double lng;
    private boolean visited;
    private LocalDateTime createdAt;
}
