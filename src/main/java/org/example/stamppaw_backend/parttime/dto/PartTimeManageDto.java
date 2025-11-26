package org.example.stamppaw_backend.parttime.dto;

import lombok.Builder;
import lombok.Data;
import org.example.stamppaw_backend.companion.entity.RecruitmentStatus;
import org.example.stamppaw_backend.parttime.entity.PartTimeApply;

import java.util.List;

@Data
@Builder
public class PartTimeManageDto {
    private String title;
    private String content;
    private String image;
    private List<PartTimeApply> applies;
    private RecruitmentStatus status;
}
