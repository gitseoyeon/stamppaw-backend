package org.example.stamppaw_backend.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String nickname;
    private String email;
    private String region;
    private String introduction;
}
