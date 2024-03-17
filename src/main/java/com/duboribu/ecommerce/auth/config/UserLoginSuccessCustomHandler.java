package com.duboribu.ecommerce.auth.config;

import com.duboribu.ecommerce.auth.domain.response.JwtTokenResponse;
import com.duboribu.ecommerce.auth.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserLoginSuccessCustomHandler implements AuthenticationSuccessHandler {
    private final AuthService authService;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 토큰 발급
        JwtTokenResponse token = authService.createJwtToken(authentication);
        createCookie(response, token.getRefreshToken());

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Bearer", token.getAccessToken());
        response.setStatus(HttpStatus.OK.value());
        response.getWriter();
    }
    private void createCookie(HttpServletResponse response, String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setMaxAge(30 * 24 * 60 * 60); // 30일
        refreshTokenCookie.setPath("/");
        response.addCookie(refreshTokenCookie);
    }
}