package org.example.stamppaw_backend.walk.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.example.stamppaw_backend.walk.entity.Walk;

import java.time.LocalDateTime;

@Getter
@Builder
public class WalkSearchResponse {

    private Long id;
    private String memo;
    private String thumbnailUrl; // 사진 1장 또는 null
    private LocalDateTime startTime;

    public static WalkSearchResponse fromEntity(Walk walk) {
        return WalkSearchResponse.builder()
                .id(walk.getId())
                .memo(walk.getMemo())
                .thumbnailUrl(
                        walk.getPhotos() != null && !walk.getPhotos().isEmpty()
                                ? walk.getPhotos().get(0).getPhotoUrl()
                                : null
                )
                .startTime(walk.getStartTime())
                .build();
    }
}