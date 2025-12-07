package com.duboribu.ecommerce.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum orderExceptionType {
    OMS_ORDER_NON_MEMBER("회원 아님", 500);
    private final String desc;
    private final int code;
}
