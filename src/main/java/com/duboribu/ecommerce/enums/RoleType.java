package com.duboribu.ecommerce.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString(of = "desc")
@RequiredArgsConstructor
public enum RoleType {
    ROLE_USER("회원"),
    ROLE_ADMIN("관리자");
    private final String desc;

}
