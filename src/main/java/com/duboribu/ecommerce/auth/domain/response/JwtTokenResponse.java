package com.duboribu.ecommerce.auth.domain.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class JwtTokenResponse {
    private final String accessToken;
    private final String refreshToken;

    public JwtTokenResponse() {
        accessToken = null;
        refreshToken = null;
    }

}
