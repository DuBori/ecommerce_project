package com.duboribu.ecommerce.auth.service;

import com.duboribu.ecommerce.auth.domain.response.JwtTokenResponse;
import com.duboribu.ecommerce.auth.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenResponse createJwtToken(final Authentication authentication) {
        JwtTokenResponse response = new JwtTokenResponse(jwtTokenProvider.createAccessToken(authentication), jwtTokenProvider.createRefreshToken());
        saveToken(authentication, response);
        return response;
    }

    private void saveToken(final Authentication authentication, JwtTokenResponse response) {
        // accessToken refreshToken db 저장 해당 회원
        log.info("db 저장할 getName : {}", authentication.getName());
    }

    public String validateAccessToken(String accessToken) {
        // 만료되지 않은 경우
        if (jwtTokenProvider.validateToken(accessToken)) {
            return "access";
        }
        return null;
    }

    public String createAccessTokenByRefreshToken(String refreshToken) {
        if (jwtTokenProvider.validateToken(refreshToken)) {
            return jwtTokenProvider.createAccessToken(refreshToken);
        }
        throw  new IllegalArgumentException("토큰 만료");
    }
}
