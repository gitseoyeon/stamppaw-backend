package org.example.stamppaw_backend.companion.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class CompanionUpdateRequest {
    @NotBlank(message = "제목은 필수로 작성해야합니다.")
    private String title;

    @NotBlank(message = "내용은 필수로 작성해야합니다.")
    private String content;

    private MultipartFile image;
}
