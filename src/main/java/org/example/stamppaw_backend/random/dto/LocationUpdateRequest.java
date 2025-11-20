package org.example.stamppaw_backend.random.dto;

import lombok.Data;

@Data
public class LocationUpdateRequest {
    private double lat;
    private double lng;
    // 클라이언트에서 보내주는 timestamp (ms 단위, System.currentTimeMillis())
    private long timestampMillis;
    private Long walkId;
}
