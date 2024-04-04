package com.duboribu.ecommerce.warehouse.order.dto.response;

import com.duboribu.ecommerce.warehouse.enums.OrderState;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UpdateWmsOrderResponse {
    private String result;
    private String coCode;
    private Long companyOrderId;
    private OrderState orderState;
    @QueryProjection
    public UpdateWmsOrderResponse(String result, String coCode, Long companyOrderId, OrderState orderState) {
        this.result = result;
        this.coCode = coCode;
        this.companyOrderId = companyOrderId;
        this.orderState = orderState;
    }
}
