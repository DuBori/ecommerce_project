package com.duboribu.ecommerce.admin.category.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateCategoryRequest {
    private Long parentId;
    private String name;
    private String code;
    private String state;
}
