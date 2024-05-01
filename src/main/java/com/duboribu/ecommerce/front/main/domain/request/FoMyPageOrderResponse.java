package com.duboribu.ecommerce.front.main.domain.request;

import com.duboribu.ecommerce.enums.OrderState;
import com.duboribu.ecommerce.front.main.domain.reponse.FoMyPageOrderItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FoMyPageOrderResponse {
    private Long orderId;
    private OrderState orderState;
    private int totalPrice;
    private int totalDcPrice;
    private List<FoMyPageOrderItem> list;
}
