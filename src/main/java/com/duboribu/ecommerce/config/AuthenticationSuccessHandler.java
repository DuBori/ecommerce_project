package com.duboribu.ecommerce.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${http.domain}")
    private String domain;
    /**
     * 기존 회원인 경우, AccessToken 발급 받은 후, 메인페이지로 이동
     * 기존 회원이 아닌 경우, 회원가입 페이지로 이동한다. 간단한 정보를 담아 간편 가입을 진행시키도록 해보자
     * */
    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Map<String, Object> attributes = ((OAuth2User) authentication.getPrincipal()).getAttributes();
        String targetUrl = UriComponentsBuilder.fromUriString(domain + "/auth/oauth2/code/google")
                .queryParam("code", attributes.get("email"))
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString();
        log.info("redirect 준비");
        // 로그인 확인 페이지로 리다이렉트 시킨다.
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}