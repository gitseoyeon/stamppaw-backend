package org.example.stamppaw_backend.parttime.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.stamppaw_backend.parttime.entity.PartTimeApply;
import org.example.stamppaw_backend.user.dto.response.UserDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartTimeApplyResponse {
    private Long id;
    private UserDto user;
    private String status;

    public static PartTimeApplyResponse fromEntity(PartTimeApply apply) {
        return PartTimeApplyResponse.builder()
            .id(apply.getId())
            .user(UserDto.fromEntity(apply.getApplicant()))
            .status(apply.getStatus().toString())
            .build();
    }
}
