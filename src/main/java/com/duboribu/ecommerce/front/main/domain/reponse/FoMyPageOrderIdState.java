package com.duboribu.ecommerce.front.main.domain.reponse;

import com.duboribu.ecommerce.enums.OrderState;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class FoMyPageOrderIdState {
    private Long orderId;
    private OrderState  orderState;
    @QueryProjection
    public FoMyPageOrderIdState(Long orderId, OrderState orderState) {
        this.orderId = orderId;
        this.orderState = orderState;
    }
}
