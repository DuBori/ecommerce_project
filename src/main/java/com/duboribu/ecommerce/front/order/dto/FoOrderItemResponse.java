package com.duboribu.ecommerce.front.order.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class FoOrderItemResponse {
    private Long itemId;
    private String itemName;
    private int itemNetPrice;
    private int itemCount;
    @QueryProjection
    public FoOrderItemResponse(Long itemId, String itemName, int itemNetPrice, int itemCount) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemNetPrice = itemNetPrice;
        this.itemCount = itemCount;
    }
}
