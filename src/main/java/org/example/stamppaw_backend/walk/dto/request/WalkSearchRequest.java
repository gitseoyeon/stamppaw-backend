package org.example.stamppaw_backend.walk.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WalkSearchRequest {
    private String keyword;   // 검색어
    private int page = 0;     // 기본값 0
    private int size = 10;    // 기본값 10
}