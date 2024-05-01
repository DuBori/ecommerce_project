package com.duboribu.ecommerce.warehouse.order.dto.response;

import com.duboribu.ecommerce.warehouse.enums.WmsOrderState;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UpdateWmsOrderResponse {
    private String result;
    private String coCode;
    private Long companyOrderId;
    private WmsOrderState wmsOrderState;
    @QueryProjection
    public UpdateWmsOrderResponse(String result, String coCode, Long companyOrderId, WmsOrderState wmsOrderState) {
        this.result = result;
        this.coCode = coCode;
        this.companyOrderId = companyOrderId;
        this.wmsOrderState = wmsOrderState;
    }
}
