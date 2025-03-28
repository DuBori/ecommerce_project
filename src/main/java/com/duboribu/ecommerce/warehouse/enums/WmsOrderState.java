package com.duboribu.ecommerce.warehouse.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum WmsOrderState {
    DELIVERY_SET("발주등록"),
    DELIVERY_READY("출고준비"),
    DELIVERY_START("출고시작"),
    DELIVERY_COMPLETE("출고완료");
    
    private final String desc;
    public static WmsOrderState getMatchState(String state) {
        return Arrays.stream(values())
                .filter(it -> it.name().equals(state))
                .findFirst()
                .get();
    }

}
