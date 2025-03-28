package com.duboribu.ecommerce.front.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FoOrderResponse {
    private String productName;
    private int totalPrice;
    private List<FoOrderItemView> list;
}
