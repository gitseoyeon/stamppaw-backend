package org.example.stamppaw_backend.community.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreateRequest {
    @NotNull
    private Long communityId;

    private Long parentId;

    @NotBlank
    private String content;
}
