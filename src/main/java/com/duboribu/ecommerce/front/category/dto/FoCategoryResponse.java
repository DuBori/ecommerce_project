package com.duboribu.ecommerce.front.category.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class FoCategoryResponse {
    private Long id;
    private String name;
    private String code;
    @QueryProjection
    public FoCategoryResponse(Long id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }
}
