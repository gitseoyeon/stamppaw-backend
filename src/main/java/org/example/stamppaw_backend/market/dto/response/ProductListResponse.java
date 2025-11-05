package org.example.stamppaw_backend.market.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductListResponse {

    private Long id;
    private String name;
    private String mainImageUrl;
    private String category;
    private String status;
    private int optionCount;
    private String price;

}
