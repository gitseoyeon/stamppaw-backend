package org.example.stamppaw_backend.companion.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class CompanionReviewCreateRequest {
    private List<Long> tags;
}
