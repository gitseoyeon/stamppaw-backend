package org.example.stamppaw_backend.market.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductSearchResponse {
    private Long id;
    private String title;
    private String content;
    private String image;
}
