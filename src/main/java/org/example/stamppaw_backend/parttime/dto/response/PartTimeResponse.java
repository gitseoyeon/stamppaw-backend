package org.example.stamppaw_backend.parttime.dto.response;

import lombok.Builder;
import lombok.Data;
import org.example.stamppaw_backend.parttime.entity.PartTime;
import org.example.stamppaw_backend.user.dto.response.UserDto;

import java.time.LocalDateTime;

@Data
@Builder
public class PartTimeResponse {
    private Long id;
    private String title;
    private String content;
    private String image;
    private String status;
    private LocalDateTime registeredAt;
    private LocalDateTime modifiedAt;
    private UserDto user;

    public static PartTimeResponse fromEntity(PartTime partTime) {
        return PartTimeResponse.builder()
            .id(partTime.getId())
            .title(partTime.getTitle())
            .content(partTime.getContent())
            .image(partTime.getImageUrl())
            .status(partTime.getStatus().toString())
            .registeredAt(partTime.getRegisteredAt())
            .modifiedAt(partTime.getModifiedAt())
            .user(UserDto.fromEntity(partTime.getUser()))
            .build();
    }
}
