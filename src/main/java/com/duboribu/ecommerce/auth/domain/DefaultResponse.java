package com.duboribu.ecommerce.auth.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class DefaultResponse<T> {
    public static final int SUCCESS = 200;
    public static final int LOGIN_ERR = 201;
    public static final int NON_MEMBER_ERR = 202;
    public static final int DUP_MEMBER_ERR = 203;
    public static final int NON_TOKEN = 204;
    public static final int EXPIRED_ACCESS_TOKEN = 205;
    public static final int EXPIRED_REFRESH_TOKEN = 206;
    public static final int SYSTEM_ERR = 500;
    public static final String SUCCESS_MSG = "정상처리";
    public static final String SYSTEM_ERR_MSG = "시스템 오류";

    private final String resMsg;
    private final int resCode;
    private final T body;

    public DefaultResponse() {
        this.resMsg = SUCCESS_MSG;
        this.resCode = SUCCESS;
        this.body = null;
    }

    public DefaultResponse(final T body) {
        this.resMsg = SUCCESS_MSG;
        this.resCode = SUCCESS;
        this.body = body;
    }

    public DefaultResponse(final String resMsg, final int resCode) {
        this.resMsg = resMsg;
        this.resCode = resCode;
        this.body = null;
    }
}
