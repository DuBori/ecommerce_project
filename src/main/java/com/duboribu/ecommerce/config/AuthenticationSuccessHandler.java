package com.duboribu.ecommerce.config;

import com.duboribu.ecommerce.auth.service.AuthService;
import com.duboribu.ecommerce.auth.util.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;

    private final AuthService authService;
    /**
     * 기존 회원인 경우, AccessToken 발급 받은 후, 메인페이지로 이동
     * 기존 회원이 아닌 경우, 회원가입 페이지로 이동한다. 간단한 정보를 담아 간편 가입을 진행시키도록 해보자
     * */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        boolean isExist = oAuth2User.getAttribute("exist");
       
        // 회원이 존재할경우
        if (isExist) {
            // 회원이 존재하면 jwt token 발행을 시작한다.
            String accessToken = jwtTokenProvider.createAccessToken(authentication);
            log.info("oauth accessToken :{}", accessToken);

            // accessToken을 쿼리스트링에 담는 url을 만들어준다.
            String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/main")
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();
            response.setHeader("Bearer", accessToken);
            // 로그인 확인 페이지로 리다이렉트 시킨다.
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        } else {
            // 회원이 존재하지 않을경우, 회원가입 페이지로 이동한다.
            String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/login/signup")
                    .queryParam("username", (String) oAuth2User.getAttribute("id"))
                    .queryParam("provider", (String)oAuth2User.getAttribute("provider"))
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();
            // 회원가입 페이지로 리다이렉트 시킨다.
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        }
    }
}