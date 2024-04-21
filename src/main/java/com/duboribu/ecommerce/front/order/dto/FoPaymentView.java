package com.duboribu.ecommerce.front.order.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class FoPaymentView {
    private Long orderId;
    private int totalNetPrice;
    private List<FoOrderItemResponse> foOrderItemResponseList;
    @QueryProjection
    public FoPaymentView(Long orderId) {
        this.orderId = orderId;
    }

    public void mactedOrderListAndTotalPrice(int totalNetPrice, List<FoOrderItemResponse> list) {
        this.totalNetPrice = totalNetPrice;
        foOrderItemResponseList = list;
    }
}
