package org.example.stamppaw_backend.parttime.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import org.springframework.web.multipart.MultipartFile;

@Data
public class PartTimeCreateRequest {
    @NotBlank(message = "제목을 입력하세요.")
    @Size(max = 50, message = "제목이 최대 길이를 넘었습니다.")
    private String title;

    @NotBlank(message = "내용을 입력하세요.")
    @Size(max = 2200, message = "내용이 최대 길이를 넘었습니다.")
    private String content;

    private MultipartFile image;
}
