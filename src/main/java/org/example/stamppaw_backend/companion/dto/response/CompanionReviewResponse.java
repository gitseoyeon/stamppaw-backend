package org.example.stamppaw_backend.companion.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.stamppaw_backend.companion.entity.CompanionReview;
import org.example.stamppaw_backend.user.dto.response.UserDto;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanionReviewResponse {
    private String title;
    private UserDto user;
    private List<String> tags;

    public static CompanionReviewResponse receivedFrom(CompanionReview companionReview) {
        return CompanionReviewResponse.builder()
                .title(companionReview.getApply().getCompanion().getTitle())
                .user(UserDto.fromEntity(companionReview.getApply().getApplicant()))
                .tags(companionReview.getTags().stream()
                        .map(m -> m.getTag().getTag())
                        .toList())
                .build();
    }

    public static CompanionReviewResponse sendFrom(CompanionReview companionReview) {
        return CompanionReviewResponse.builder()
                .title(companionReview.getApply().getCompanion().getTitle())
                .user(UserDto.fromEntity(companionReview.getApply().getCompanion().getUser()))
                .tags(companionReview.getTags().stream()
                        .map(m -> m.getTag().getTag())
                        .toList())
                .build();
    }
}
