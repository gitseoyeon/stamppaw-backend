package org.example.stamppaw_backend.walk.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class WalkRecordRequest {
    private String memo;
    private List<MultipartFile> photos;
}