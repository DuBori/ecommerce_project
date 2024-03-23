package com.duboribu.ecommerce.auth.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
@Getter
@RequiredArgsConstructor
public enum UserType {
    JOIN_MEMBER("회원가입"),
    LOGIN_MEMBER("로그인");
    
    private final String desc;
}
