package com.duboribu.ecommerce.auth.service;

import com.duboribu.ecommerce.Utils.RestUtil;
import com.duboribu.ecommerce.auth.social.GooGleProfile;
import com.duboribu.ecommerce.auth.social.kakao.dto.request.KaKaoTokenRequst;
import com.duboribu.ecommerce.auth.social.kakao.dto.response.KakaoProfile;
import com.duboribu.ecommerce.auth.social.kakao.dto.response.KakaoToken;
import com.duboribu.ecommerce.auth.social.naver.request.NaverTokenRequest;
import com.duboribu.ecommerce.auth.social.naver.response.NaverProfile;
import com.duboribu.ecommerce.auth.social.naver.response.NaverToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor
public class SocialAuthService {
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Value("${spring.security.oauth2.client.provider.kakao.token_uri}")
    private String kakaoTokenUri;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String kakaoUserInfoUri;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String naverClientSecret;

    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String naverRedirectUri;

    @Value("${spring.security.oauth2.client.provider.naver.token_uri}")
    private String naverTokenUri;

    @Value("${spring.security.oauth2.client.provider.naver.user-info-uri}")
    private String naverUserInfoUri;

    public NaverProfile naverProcess(String code) {
        NaverToken userToken = getUserTokenByNaverCode(code);
        NaverProfile naverProfile = findNaverProfile(userToken.getAccess_token());
        return naverProfile;
    }

    private NaverToken getUserTokenByNaverCode(String code) {
        NaverTokenRequest kaKaoRequestProfile = new NaverTokenRequest(naverClientId, naverRedirectUri, naverClientSecret, code);
        ResponseEntity<NaverToken> response = RestUtil.post(naverTokenUri, kaKaoRequestProfile,  new HttpHeaders(), NaverToken.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        throw new IllegalArgumentException();
    }
    public NaverProfile findNaverProfile(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        ResponseEntity<NaverProfile> response = RestUtil.get(naverUserInfoUri, headers, NaverProfile.class);
        if (response.getStatusCode() == HttpStatus.OK && "success".equals(response.getBody().getMessage())) {
            return response.getBody();
        }
        throw new IllegalArgumentException();
    }

    public KakaoProfile kakaoProcess(String code) {
        KakaoToken kakaoToken = getKaKaoTokenByCode(code);
        log.info("{}", kakaoToken);
        KakaoProfile profile = findProfile(kakaoToken.getAccess_token());
        log.info("{}", profile);
        return profile;
    }

    public KakaoToken getKaKaoTokenByCode(String code) {
        KaKaoTokenRequst kaKaoTokenRequst = new KaKaoTokenRequst(kakaoClientId, kakaoRedirectUri, code);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        ResponseEntity<KakaoToken> response = RestUtil.post(kakaoTokenUri, kaKaoTokenRequst, headers, KakaoToken.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        throw new IllegalArgumentException();
    }

    private KakaoProfile findProfile(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        ResponseEntity<KakaoProfile> response = RestUtil.get(kakaoUserInfoUri, headers, KakaoProfile.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        throw new IllegalArgumentException();
    }

    public GooGleProfile googleProcess(String code) {
        return new GooGleProfile(code);
    }

}
