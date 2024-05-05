package com.duboribu.ecommerce.front.main.domain.request;

import com.duboribu.ecommerce.enums.OrderState;
import com.duboribu.ecommerce.front.main.domain.reponse.FoMyPageOrderItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@NoArgsConstructor
public class FoMyPageOrderResponse {
    private Long orderId;
    private String orderState;
    private int totalPrice;
    private int totalDcPrice;
    private List<FoMyPageOrderItem> list;

    public FoMyPageOrderResponse(Long orderId, OrderState orderState, int totalPrice, int totalDcPrice, List<FoMyPageOrderItem> list) {
        this.orderId = orderId;
        this.orderState = orderState.getDesc();
        this.totalPrice = totalPrice;
        this.totalDcPrice = totalDcPrice;
        this.list = list;
    }
}
