package com.duboribu.ecommerce.warehouse.order.dto.response;

import com.duboribu.ecommerce.warehouse.enums.OrderState;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UpdateWmsOrderResponse {
    private String result;
    private Long companyOrderId;
    private OrderState orderState;
    @QueryProjection
    public UpdateWmsOrderResponse(String result, Long companyOrderId, OrderState orderState) {
        this.result = result;
        this.companyOrderId = companyOrderId;
        this.orderState = orderState;
    }
}
