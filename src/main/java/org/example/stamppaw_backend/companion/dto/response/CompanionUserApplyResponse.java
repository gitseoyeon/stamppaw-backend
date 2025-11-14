package org.example.stamppaw_backend.companion.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.stamppaw_backend.companion.dto.CompanionDto;
import org.example.stamppaw_backend.companion.entity.CompanionApply;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanionUserApplyResponse {
    private CompanionDto companionDto;
    private String status;

    public static CompanionUserApplyResponse from(CompanionApply apply) {
        return CompanionUserApplyResponse.builder()
                .companionDto(CompanionDto.from(apply.getCompanion()))
                .status(apply.getStatus().toString())
                .build();
    }
}
