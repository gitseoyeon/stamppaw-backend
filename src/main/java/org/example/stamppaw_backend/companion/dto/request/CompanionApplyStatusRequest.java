package org.example.stamppaw_backend.companion.dto.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.stamppaw_backend.companion.entity.ApplyStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanionApplyStatusRequest {
    @Enumerated(EnumType.STRING)
    private ApplyStatus status;
}
