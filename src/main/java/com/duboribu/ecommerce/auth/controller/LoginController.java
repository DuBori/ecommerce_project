package com.duboribu.ecommerce.auth.controller;

import com.duboribu.ecommerce.auth.domain.DefaultResponse;
import com.duboribu.ecommerce.auth.domain.UserDto;
import com.duboribu.ecommerce.auth.domain.request.JwtLoginRequest;
import com.duboribu.ecommerce.auth.domain.response.JwtTokenResponse;
import com.duboribu.ecommerce.auth.service.AuthService;
import com.duboribu.ecommerce.auth.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController(value = "LoginAPIController")
@RequestMapping("/join")
@RequiredArgsConstructor
public class LoginController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberService memberService;
    private final AuthService authService;

    @PostMapping("/sign-up")
    @Operation(summary = "회원가입", description = "회원 정보가 필요합니다")
    public ResponseEntity<UserDto> signUp(@RequestBody UserDto userDto) {
        UserDto join = memberService.join(userDto);
        if (join.getUsername() == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping("/sign-in")
    @Operation(summary = "로그인", description = "회원 정보가 필요합니다")
    public ResponseEntity<DefaultResponse<JwtTokenResponse>> signIn(@RequestBody final JwtLoginRequest request, HttpServletResponse response) {
        final Authentication authentication = authenticationManagerBuilder.getObject().authenticate(request.toAuthentication());
        final JwtTokenResponse token = authService.createJwtToken(authentication);
        createCookie(response, token.getRefreshToken());
        return new ResponseEntity<>(new DefaultResponse<>(token), settingHeader(token), HttpStatus.OK);
    }
    private void createCookie(HttpServletResponse response, String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setMaxAge(30 * 24 * 60 * 60); // 30일
        refreshTokenCookie.setPath("/");
        response.addCookie(refreshTokenCookie);
    }
    private HttpHeaders settingHeader(JwtTokenResponse token) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token.getAccessToken());
        return headers;
    }
}
