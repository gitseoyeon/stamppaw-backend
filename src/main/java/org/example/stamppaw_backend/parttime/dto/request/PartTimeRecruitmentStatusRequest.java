package org.example.stamppaw_backend.parttime.dto.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.stamppaw_backend.companion.entity.RecruitmentStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartTimeRecruitmentStatusRequest {
    @Enumerated(EnumType.STRING)
    RecruitmentStatus status;
}
