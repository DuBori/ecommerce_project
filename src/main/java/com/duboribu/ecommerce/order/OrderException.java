package com.duboribu.ecommerce.order;

import lombok.Getter;

@Getter
public class OrderException  extends RuntimeException {
    private final int code;

    public OrderException(final orderExceptionType orderExceptionType) {
        super(orderExceptionType.getDesc());
        this.code = orderExceptionType.getCode();
    }
}