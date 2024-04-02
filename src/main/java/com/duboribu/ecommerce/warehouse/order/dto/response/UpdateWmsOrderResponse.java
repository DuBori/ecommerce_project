package com.duboribu.ecommerce.warehouse.order.dto.response;

import com.duboribu.ecommerce.warehouse.enums.OrderState;
import lombok.Getter;

@Getter
public class UpdateWmsOrderResponse {
    private String result;
    private Long companyOrderId;
    private OrderState orderState;

    public UpdateWmsOrderResponse(String result, Long companyOrderId, OrderState orderState) {
        this.result = result;
        this.companyOrderId = companyOrderId;
        this.orderState = orderState;
    }
}
