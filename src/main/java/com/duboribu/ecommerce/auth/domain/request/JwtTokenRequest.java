package com.duboribu.ecommerce.auth.domain.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class JwtTokenRequest {
    private final String accessToken;
    private final String refreshToken;

    public JwtTokenRequest() {
        accessToken = null;
        refreshToken = null;
    }
}
