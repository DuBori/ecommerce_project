package com.duboribu.ecommerce.admin.item.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SearchItemRequest {
    private String searchType;
    private String keyword;
    private int page;
    private int pageSize = 2;

}
