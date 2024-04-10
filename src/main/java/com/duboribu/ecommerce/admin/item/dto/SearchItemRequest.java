package com.duboribu.ecommerce.admin.item.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SearchItemRequest {
    private String name;
    private String author;
    private int page;
    private int pageSize = 20;

}
