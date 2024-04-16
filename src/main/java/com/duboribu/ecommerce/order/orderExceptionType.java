package com.duboribu.ecommerce.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum orderExceptionType {
    ;
    private final String desc;
    private final int code;
}
