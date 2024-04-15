package com.duboribu.ecommerce.admin.category.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchCategory {
    private int page;
    private int size = 20;
}
