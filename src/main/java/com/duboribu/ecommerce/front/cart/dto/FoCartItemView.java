package com.duboribu.ecommerce.front.cart.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class FoCartItemView {
    private Long itemId;
    private int quantity;
    @QueryProjection
    public FoCartItemView(Long itemId, int quantity) {
        this.itemId = itemId;
        this.quantity = quantity;
    }
}
