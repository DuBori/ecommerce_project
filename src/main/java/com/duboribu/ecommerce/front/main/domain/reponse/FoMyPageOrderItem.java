package com.duboribu.ecommerce.front.main.domain.reponse;

import com.duboribu.ecommerce.enums.OrderState;
import com.duboribu.ecommerce.warehouse.enums.WmsOrderState;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class FoMyPageOrderItem {
    private Long orderId;
    private OrderState state;
    private WmsOrderState wmsOrderState;
    private String name;
    private int price;
    private int quantity;
    private int unitPrice;
    private int dcPrice;
    @QueryProjection
    public FoMyPageOrderItem(Long orderId, OrderState state, WmsOrderState wmsOrderState, String name, int price, int quantity, int unitPrice, int dcPrice) {
        this.orderId = orderId;
        this.state = state;
        this.wmsOrderState = wmsOrderState;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.dcPrice = dcPrice;
    }
}
