package com.duboribu.ecommerce.config;

import com.duboribu.ecommerce.auth.util.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//로그인 인증 처리 커스텀 필터
@Slf4j
@RequiredArgsConstructor
public class JwtCustomFilter extends OncePerRequestFilter {
    private final JwtTokenProvider tokenProvider;
    public static final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        log.info("path : {}", path);
        log.info("Token 검증 필터 진입");
        // 추후에 수정 필요부분 모두 검증로직타버림
        /*String headerToken = request.getHeader(AUTHORIZATION_HEADER);
        // 토큰 검사 생략(모두 허용 URL의 경우 토큰 검사 통과)
        if (!StringUtils.hasText(headerToken)) {
            doFilter(request, response, filterChain);
            return;
        }*/
        if (path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs") || path.startsWith("/swagger-resources")) {
            log.info("swagger 진입");
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = "";
        Cookie[] cookies = request.getCookies();
        if (request.getCookies() != null) {
            for (Cookie cookie : cookies) {
                if (AUTHORIZATION_HEADER.equals(cookie.getName())) {
                    accessToken = cookie.getValue();
                }
            }
        }
        if (accessToken.isEmpty()) {
            doFilter(request, response, filterChain);
            return;
        }
        log.info("accessToken : {}", accessToken);
        String requestURI = request.getRequestURI();
        if (!tokenProvider.validateToken(accessToken)) {
            throw new JwtException("Access Token 만료");
        }

        if (tokenProvider.validateToken(accessToken)) {
            Authentication authentication = tokenProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
        }

        filterChain.doFilter(request, response);
    }
}