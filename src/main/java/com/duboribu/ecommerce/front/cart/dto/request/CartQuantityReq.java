package com.duboribu.ecommerce.front.cart.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CartQuantityReq {
    private Long itemId;
    private Long cartItemId;
}
