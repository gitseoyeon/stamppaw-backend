package org.example.stamppaw_backend.companion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.stamppaw_backend.companion.entity.Companion;
import org.example.stamppaw_backend.companion.entity.RecruitmentStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanionDto {
    private String title;
    private RecruitmentStatus status;

    public static CompanionDto from(Companion companion) {
        return CompanionDto.builder()
                .title(companion.getTitle())
                .status(companion.getStatus())
                .build();
    }
}
