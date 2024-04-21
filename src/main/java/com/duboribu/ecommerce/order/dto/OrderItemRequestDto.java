package com.duboribu.ecommerce.order.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderItemRequestDto {
    private Long productId;
    private int quantity;
    private String productName;
    private String productPrice;
}
