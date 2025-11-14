package org.example.stamppaw_backend.companion.dto;

import lombok.Builder;
import lombok.Data;
import org.example.stamppaw_backend.companion.entity.CompanionApply;
import org.example.stamppaw_backend.companion.entity.RecruitmentStatus;

import java.util.List;

@Data
@Builder
public class CompanionManageDto {
    private String title;
    private String content;
    private String image;
    private List<CompanionApply> applies;
    private RecruitmentStatus status;
}
