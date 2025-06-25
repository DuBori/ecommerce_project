package com.duboribu.ecommerce.auth.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtUserExceptionType {
    REQUEST_EMPTY_NAME("이름이 없습니다.", 101),
    REQUEST_EMPTY_PWD("비밀번호가 없습니다.", 102),
    NON_USER("회원이 아닙니다.", 103),
    AUTHENTICATION_EMPTY("인증 파라미터가 없습니다.", 104),
    WRONG_JWT_SIGN("잘못된 서명입니다.", 105),
    EXPIRED_JWT_TOKEN("만료된 JWT 토큰입니다.", 106),
    UN_SUPPORTED_JWT_TOKEN("지원되지 않는 JWT 토큰입니다.", 107),
    ILLEGAL_JWT_TOKEN("JWT 토큰이 잘못되었습니다.", 108),
    NON_TOKEN("토큰이 존재하지 않습니다.", 109),
    INVALID_CLASS("토큰의 정보가 존재하지 않습니다.", 110);

    private final String desc;
    private final int code;


}
