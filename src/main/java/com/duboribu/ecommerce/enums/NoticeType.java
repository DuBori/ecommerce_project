package com.duboribu.ecommerce.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NoticeType {
    NOTICE("공지사항"),
    QNA("1:1문의");
    
    private final String desc;
}
