package com.duboribu.ecommerce.front.order.dto;

import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    private int totalPrice;
    private int usePoint;
    private List<OrderItemRequest> orderItemRequest;
    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    public static class OrderItemRequest {
        private Long productId;
        private int quantity;

        public OrderItemRequest(Long productId, int quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }
    }


    public List<Long> getProductIds() {
        if (orderItemRequest.isEmpty()) {
            return null;
        }
        return orderItemRequest.stream()
                .map(it -> it.getProductId())
                .collect(Collectors.toList());
    }
}
