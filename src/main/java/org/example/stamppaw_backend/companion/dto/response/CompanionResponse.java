package org.example.stamppaw_backend.companion.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.stamppaw_backend.companion.entity.Companion;
import org.example.stamppaw_backend.companion.entity.RecruitmentStatus;
import org.example.stamppaw_backend.user.dto.response.UserDto;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanionResponse {
    private Long id;
    private String title;
    private String content;
    private String image;
    private LocalDateTime registeredAt;
    private LocalDateTime modifiedAt;
    private RecruitmentStatus status;
    private UserDto user;


    public static CompanionResponse fromEntity(Companion companion) {
        return CompanionResponse.builder()
                .id(companion.getId())
                .title(companion.getTitle())
                .content(companion.getContent())
                .image(companion.getImageUrl())
                .registeredAt(companion.getRegisteredAt())
                .modifiedAt(companion.getModifiedAt())
                .status(companion.getStatus())
                .user(UserDto.fromEntity(companion.getUser()))
                .build();
    }
}
