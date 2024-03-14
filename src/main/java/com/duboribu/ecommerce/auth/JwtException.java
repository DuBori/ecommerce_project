package com.duboribu.ecommerce.auth;

import com.duboribu.ecommerce.auth.enums.JwtUserExceptionType;
import lombok.Getter;

@Getter
public class JwtException extends RuntimeException {
    private final int code;

    public JwtException(final JwtUserExceptionType jwtUserExceptionType) {
        super(jwtUserExceptionType.getDesc());
        this.code = jwtUserExceptionType.getCode();
    }

}
