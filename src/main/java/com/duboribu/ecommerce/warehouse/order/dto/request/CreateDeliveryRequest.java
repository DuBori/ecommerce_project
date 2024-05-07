package com.duboribu.ecommerce.warehouse.order.dto.request;

import com.duboribu.ecommerce.warehouse.entity.WmsOrder;
import com.duboribu.ecommerce.warehouse.entity.WmsOrderItem;
import com.duboribu.ecommerce.warehouse.enums.WmsOrderState;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@ToString
public class CreateDeliveryRequest {
    private static String date; // YYYYMMDD 필수값아님 예외가 필요한 경우
    private static String code; // 회사코드
    private List<OrderInfo> orderList;

    @Getter
    @Setter
    @ToString
    public static class OrderInfo {
        private Long orderId;
        private Long orderItemId;
        private int count;
        private DeliveryInfo deliveryInfo;
        @Getter
        @Setter
        @ToString
        public static class DeliveryInfo {
            private String zip;
            private String address;

            public DeliveryInfo(String zip, String address) {
                this.zip = zip;
                this.address = address;
            }
        }

        public OrderInfo(Long orderId, Long orderItemId, int count, DeliveryInfo deliveryInfo) {
            this.orderId = orderId;
            this.orderItemId = orderItemId;
            this.count = count;
            this.deliveryInfo = deliveryInfo;
        }

        public WmsOrder toEntity() {
            return new WmsOrder(orderId, date, code, Collections.singletonList(new WmsOrderItem(orderItemId, WmsOrderState.DELIVERY_SET)));
        }
    }

    public CreateDeliveryRequest(String date, String code, List<OrderInfo> orderList) {
        this.date = date;
        this.code = code;
        this.orderList = orderList;
    }
}