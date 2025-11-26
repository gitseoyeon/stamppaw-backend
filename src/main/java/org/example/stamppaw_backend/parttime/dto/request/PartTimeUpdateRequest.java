package org.example.stamppaw_backend.parttime.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PartTimeUpdateRequest {
    private String title;
    private String content;
    private MultipartFile image;
    private Boolean deleteImage;
}
