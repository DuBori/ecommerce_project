package com.duboribu.ecommerce.auth.social.naver.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class NaverTokenRequest {
    private String grant_type = "authorization_code";
    private final String client_id ;
    private final String redirect_uri;
    private final String client_secret;
    private final String code;

    public NaverTokenRequest() {
        this.client_id = null;
        this.redirect_uri = null;
        client_secret = null;
        this.code = null;
    }


}
