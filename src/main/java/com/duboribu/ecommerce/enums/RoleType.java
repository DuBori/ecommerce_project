package com.duboribu.ecommerce.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString(of = "desc")
@RequiredArgsConstructor
public enum RoleType {
    ROLE_USER("user", 1L),
    ROLE_ADMIN("admin", 2L);
    private final String desc;
    private final Long dbValue;

}
