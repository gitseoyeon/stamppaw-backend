package org.example.stamppaw_backend.parttime.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.stamppaw_backend.companion.dto.CompanionDto;
import org.example.stamppaw_backend.parttime.dto.PartTimeDto;
import org.example.stamppaw_backend.parttime.entity.PartTimeApply;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartTimeUserApplyResponse {

    private Long id;
    private Long partTimeId;
    private String title;
    private String imageUrl;    // 이미지
    private PartTimeDto partTimeDto;
    private String status;

    public static PartTimeUserApplyResponse from(PartTimeApply apply) {
        return PartTimeUserApplyResponse.builder()
            .id(apply.getId())
            .partTimeId(apply.getPartTime().getId())
            .partTimeDto(PartTimeDto.from(apply.getPartTime()))
            .status(apply.getStatus().toString())
            .build();
    }
}
