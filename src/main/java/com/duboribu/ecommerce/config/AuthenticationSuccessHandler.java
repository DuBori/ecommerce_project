package com.duboribu.ecommerce.config;

import com.duboribu.ecommerce.auth.service.MemberService;
import com.duboribu.ecommerce.auth.service.MemberTokenService;
import com.duboribu.ecommerce.auth.util.JwtTokenProvider;
import com.duboribu.ecommerce.entity.Member;
import com.duboribu.ecommerce.entity.MemberToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;

    private final MemberService memberService;
    private final MemberTokenService memberTokenService;
    /**
     * 기존 회원인 경우, AccessToken 발급 받은 후, 메인페이지로 이동
     * 기존 회원이 아닌 경우, 회원가입 페이지로 이동한다. 간단한 정보를 담아 간편 가입을 진행시키도록 해보자
     * */
    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        boolean isExist = oAuth2User.getAttribute("exist");
       
        // 회원이 존재할경우
        if (isExist) {
            // 회원이 존재하면 jwt token 발행을 시작한다.
            String accessToken = jwtTokenProvider.createAccessToken(authentication);
            log.info("oauth accessToken :{}", accessToken);
            log.info("oauth name : {}", oAuth2User.getName());
            Member member = memberService.findById(oAuth2User.getName()).get();
            MemberToken refreshToken = memberTokenService.save(new MemberToken(jwtTokenProvider.createRefreshToken()));
            member.updateToken(refreshToken);

            // accessToken을 쿼리스트링에 담는 url을 만들어준다.
            String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/main")
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();

            response.setHeader("Authorization", "Bearer " + accessToken);
            createCookie(response, refreshToken.getRefreshToken());
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        } else {
            // 회원이 존재하지 않을경우, 회원가입 페이지로 이동한다.
            String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/login/signup")
                    .queryParam("username", (String) oAuth2User.getAttribute("email"))
                    .queryParam("provider", (String)oAuth2User.getAttribute("provider"))
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();
            // 회원가입 페이지로 리다이렉트 시킨다.
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        }
    }
    private void createCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(30 * 24 * 60 * 60); // 30일
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}