package com.duboribu.ecommerce.auth.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class DefaultResponse<T> {
    public static final int SUCCESS = 200;
    public static final String SUCCESS_MSG = "정상처리";
    public static final int SYSTEM_ERR = 500;
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
