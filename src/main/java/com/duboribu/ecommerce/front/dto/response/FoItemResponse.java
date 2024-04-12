package com.duboribu.ecommerce.front.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString
public class FoItemResponse {
    private Long id;
    private String name;
    private int price;
    private String filePath;
    @QueryProjection
    public FoItemResponse(Long id, String name, BigDecimal price, String filePath) {
        this.id = id;
        this.name = name;
        this.price = price.intValue();
        this.filePath = filePath;
    }
}
