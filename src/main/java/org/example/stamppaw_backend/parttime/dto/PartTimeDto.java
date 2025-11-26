package org.example.stamppaw_backend.parttime.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.stamppaw_backend.companion.entity.RecruitmentStatus;
import org.example.stamppaw_backend.parttime.entity.PartTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartTimeDto {
    private String title;
    private RecruitmentStatus status;

    public static PartTimeDto from(PartTime partTime) {
        return PartTimeDto.builder()
            .title(partTime.getTitle())
            .status(partTime.getStatus())
            .build();
    }
}
