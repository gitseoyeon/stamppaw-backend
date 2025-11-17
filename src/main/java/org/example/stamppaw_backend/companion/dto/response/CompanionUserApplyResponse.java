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
    private Long id;
    private Long companionId;
    private CompanionDto companionDto;
    private String status;
    private boolean isReviewWritten;

    public static CompanionUserApplyResponse from(CompanionApply apply, boolean isReviewWritten) {
        return CompanionUserApplyResponse.builder()
                .id(apply.getId())
                .companionId(apply.getCompanion().getId())
                .companionDto(CompanionDto.from(apply.getCompanion()))
                .status(apply.getStatus().toString())
                .isReviewWritten(isReviewWritten)
                .build();
    }
}
