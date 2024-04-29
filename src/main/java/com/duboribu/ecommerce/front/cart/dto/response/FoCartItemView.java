package com.duboribu.ecommerce.front.cart.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class FoCartItemView {
    private Long itemId;
    private Long cartItemId;
    private int quantity;
    @QueryProjection
    public FoCartItemView(Long itemId, Long cartItemId, int quantity) {
        this.itemId = itemId;
        this.cartItemId = cartItemId;
        this.quantity = quantity;
    }
}
