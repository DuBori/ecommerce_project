package com.duboribu.ecommerce.order.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class OrderRequestDTO {
    private String impUid;
    private String merchantUid;
    private String userName;
    private String userId;
    private String buyerAddr;
    private String phone;
    private String email;
    private String cart;
    private List<OrderItemRequestDto> productItems;
}
