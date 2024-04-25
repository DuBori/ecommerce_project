package com.duboribu.ecommerce.front.cart;

import com.duboribu.ecommerce.front.order.dto.CreateOrderRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public class CartsRequest {
    private List<CartRequest> list;

    public List<CreateOrderRequest.OrderItemRequest> toOrderRequest() {
        if(!list.isEmpty()) {
            return list.stream()
                    .map(it -> new CreateOrderRequest.OrderItemRequest(it.getProductId(), it.getQuantity()))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
