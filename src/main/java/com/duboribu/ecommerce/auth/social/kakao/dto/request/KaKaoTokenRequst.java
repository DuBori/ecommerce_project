package com.duboribu.ecommerce.auth.social.kakao.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class KaKaoTokenRequst {
    private String grant_type = "authorization_code";
    private final String client_id ;
    private final String redirect_uri;
    private final String code;

    public KaKaoTokenRequst() {
        this.client_id = null;
        this.redirect_uri = null;
        this.code = null;
    }


}
