package com.duboribu.ecommerce.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
@Getter
@RequiredArgsConstructor
public enum OrderState {
    OSI01("결제대기"),
    OSI02("결제완료"),
    OSI03("상품준비중"),
    OSI04("배송중"),
    OSI05("배송완료"),
    OSI06("구매확정"),
    OSC01("취소신청"),
    OSC02("취소완료");

    private final String desc;
}
