package org.example.stamppaw_backend.companion.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.stamppaw_backend.companion.entity.CompanionApply;
import org.example.stamppaw_backend.user.dto.response.UserDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanionApplyResponse {
    private Long id;
    private UserDto user;
    private String status;

    public static CompanionApplyResponse fromEntity(CompanionApply companionApply) {
        return CompanionApplyResponse.builder()
                .id(companionApply.getId())
                .user(UserDto.fromEntity(companionApply.getApplicant()))
                .status(companionApply.getStatus().toString())
                .build();
    }
}
