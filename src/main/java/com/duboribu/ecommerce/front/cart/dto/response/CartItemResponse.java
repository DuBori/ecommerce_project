package com.duboribu.ecommerce.front.cart.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponse {
    private int quantity;
    private int unitPrice;
}
