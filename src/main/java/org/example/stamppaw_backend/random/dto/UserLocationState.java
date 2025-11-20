package org.example.stamppaw_backend.random.dto;

import lombok.*;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class UserLocationState {
    private double Lat;
    private double Lng;
    private long TimestampMillis;
    private double accumulatedDistanceMeters;

    public UserLocationState(double lat, double lng, long time) {
        this.Lat = lat;
        this.Lng = lng;
        this.TimestampMillis = time;
        this.accumulatedDistanceMeters = 0.0;
    }
}