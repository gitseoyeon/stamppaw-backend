package org.example.stamppaw_backend.parttime.dto.request;

import lombok.Data;
import org.example.stamppaw_backend.parttime.entity.PartTimeStatus;

@Data
public class PartTimeStatusRequest {
    private PartTimeStatus status;
}
