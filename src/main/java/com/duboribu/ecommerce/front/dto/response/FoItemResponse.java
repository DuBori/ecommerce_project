package com.duboribu.ecommerce.front.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class FoItemResponse {
    private Long id;
    private String name;
    private int dcrt;
    private int price;
    private String filePath;
    @QueryProjection
    public FoItemResponse(Long id, String name, Integer dcrt, Integer price, String filePath) {
        this.id = id;
        this.name = name;
        if (dcrt == null) {
            this.dcrt = 0;
        } else {
            this.dcrt = dcrt;
        }
        this.price = price;
        this.filePath = filePath;
    }
}
