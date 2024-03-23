package com.duboribu.ecommerce.auth.service;

import com.duboribu.ecommerce.auth.domain.response.JwtTokenResponse;
import com.duboribu.ecommerce.auth.util.JwtTokenProvider;
import com.duboribu.ecommerce.entity.Member;
import com.duboribu.ecommerce.entity.MemberToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberTokenService memberTokenService;
    @Transactional
    public JwtTokenResponse createToken(Member member) {
        if (member.getMemberToken() != null) {
            memberTokenService.delete(member.getMemberToken());
        }
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getId());
        member.updateToken(memberTokenService.save(new MemberToken(refreshToken)));
        String accessToken = jwtTokenProvider.createAccessToken(refreshToken);
        return new JwtTokenResponse(accessToken, refreshToken);
    }
}
