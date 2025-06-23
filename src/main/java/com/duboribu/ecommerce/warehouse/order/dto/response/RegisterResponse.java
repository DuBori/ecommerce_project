package com.duboribu.ecommerce.warehouse.order.dto.response;

import com.duboribu.ecommerce.warehouse.enums.WmsOrderState;
import lombok.*;

import java.util.List;

@Getter
@ToString
public class RegisterResponse {
    private int totalCount;
    private List<WmsOrderResponse> list;

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WmsOrderResponse {
        private Long orderId;
        private Long orderItemId;
        private WmsOrderState wmsOrderState;

    }

    public RegisterResponse(int totalCount, List<WmsOrderResponse> list) {
        this.totalCount = totalCount;
        this.list = list;
    }
}
