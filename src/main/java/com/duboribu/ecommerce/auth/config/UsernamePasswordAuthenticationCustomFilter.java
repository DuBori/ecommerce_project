package com.duboribu.ecommerce.auth.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

//로그인 인증 처리 커스텀 필터
@Slf4j
@RequiredArgsConstructor
public class UsernamePasswordAuthenticationCustomFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final UserLoginSuccessCustomHandler successHandler;
    private final UserLoginFailureCustomHandler failureHandler;


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //1. body 에서 로그인 정보 받아오기
        /*if (ObjectUtils.isEmpty()) {
            throw new JwtException(JwtUserExceptionType.AUTHENTICATION_EMPTY);
        }*/

        //2. Login ID, Pass 를 기반으로 AuthenticationToken 생성


        //3. User Password 인증이 이루어지는 부분
        //"authenticate" 가 실행될때 "PrincipalDetailService.loadUserByUsername" 실행
        log.info("UsernamePasswordAuth Come in");
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        this.successHandler.onAuthenticationSuccess(request, response, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        this.failureHandler.onAuthenticationFailure(request,response, failed);
    }
}