package com.duboribu.ecommerce.warehouse.order.dto;

import com.duboribu.ecommerce.warehouse.enums.WmsOrderState;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class WmsOrderInfo {
        private String date;
        private int totalPageSize;
        private int currentPage;
        private List<WmsOrderResponse> list;

        @Getter
        @Setter
        @ToString
        public class WmsOrderResponse {
            private String orderId;
            private String orderItemId;
            private WmsOrderState wmsOrderState;

        }
    }
