package org.example.stamppaw_backend.market.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSearchRequest
{
    private String keyword;
    private int page = 0;
    private int size = 10;
}
