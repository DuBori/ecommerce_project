package com.duboribu.ecommerce.warehouse.order.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class ProcessDeliveryRequest {
    private String newOrderState;
    private List<CoDeliveryInfo> orderItemId;

    @Setter
    @Getter
    @ToString
    public static class CoDeliveryInfo {
        private Long orderItemId;
        private String coCode;

        public Long getOrderItemId() {
            return orderItemId;
        }

        public String getCoCode() {
            return coCode;
        }

        public CoDeliveryInfo(Long orderItemId, String coCode) {
            this.orderItemId = orderItemId;
            this.coCode = coCode;
        }
    }

    public String getNewOrderState() {
        return newOrderState;
    }

    public List<CoDeliveryInfo> getOrderItemId() {
        return orderItemId;
    }

    public ProcessDeliveryRequest(String newOrderState, List<CoDeliveryInfo> orderItemId) {
        this.newOrderState = newOrderState;
        this.orderItemId = orderItemId;
    }
}
