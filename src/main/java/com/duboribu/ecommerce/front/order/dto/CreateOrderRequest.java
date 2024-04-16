package com.duboribu.ecommerce.front.order.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    private int totalPrice;
    private int usePoint;
    private List<OrderBook> orderBook;
    @Getter
    @Setter
    @ToString
    private class OrderBook {
        private Long id;
        private int count;
        private int price;
    }
}
