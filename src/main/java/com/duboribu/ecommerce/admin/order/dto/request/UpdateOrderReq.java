package com.duboribu.ecommerce.admin.order.dto.request;

import com.duboribu.ecommerce.enums.OrderState;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdateOrderReq {
    private Long id;
    private String orderState;

    public OrderState getOrderState() {
        return OrderState.valueOf(orderState);
    }
}
