package com.duboribu.ecommerce.admin.order.dto.response;

import com.duboribu.ecommerce.enums.OrderState;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AdminOrderView {
    private Long orderId;
    private String memberName;
    private String orderState;
    @QueryProjection
    public AdminOrderView(Long orderId, String memberName, OrderState orderState) {
        this.orderId = orderId;
        this.memberName = memberName;
        this.orderState = orderState.getDesc();
    }
}
