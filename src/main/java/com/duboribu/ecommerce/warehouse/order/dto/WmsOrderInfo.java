package com.duboribu.ecommerce.warehouse.order.dto;

import com.duboribu.ecommerce.warehouse.enums.WmsOrderState;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class WmsOrderInfo {
        private String date;
        private int totalPageSize;
        private int currentPage;
        @Getter
        @Setter
        @ToString
        public  class WmsOrderResponse {
            private String orderId;
            private String orderItemId;
            private WmsOrderState wmsOrderState;

        }
    }
