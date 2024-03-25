package com.duboribu.ecommerce.auth.service;

import com.duboribu.ecommerce.auth.JwtException;
import com.duboribu.ecommerce.auth.domain.response.JwtTokenResponse;
import com.duboribu.ecommerce.auth.domain.response.UserResponse;
import com.duboribu.ecommerce.auth.enums.JwtUserExceptionType;
import com.duboribu.ecommerce.auth.repository.MemberJpaRepository;
import com.duboribu.ecommerce.auth.repository.MemberTokenJpaRepository;
import com.duboribu.ecommerce.auth.util.JwtTokenProvider;
import com.duboribu.ecommerce.entity.Member;
import com.duboribu.ecommerce.entity.MemberToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberTokenService {
    private final MemberTokenJpaRepository memberTokenJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final JwtTokenProvider jwtTokenProvider;
    public void delete(MemberToken memberToken) {
        memberTokenJpaRepository.delete(memberToken);
    }

    public MemberToken save(MemberToken memberToken) {
        return memberTokenJpaRepository.save(memberToken);
    }
    @Transactional
    public UserResponse validateToken(String accessToken) {
        if (!StringUtils.hasText(accessToken)) {
            return null;
        }
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        Optional<Member> findMember = memberJpaRepository.findById(authentication.getName());
        if (findMember.isEmpty()) {
            throw new JwtException(JwtUserExceptionType.ILLEGAL_JWT_TOKEN);
        }
        Member member = findMember.get();
        String refreshToken = member.getMemberToken().getRefreshToken();
        if (jwtTokenProvider.isExpired(accessToken) && jwtTokenProvider.validateToken(refreshToken)) {
            return new UserResponse(member.getMemberDto(), new JwtTokenResponse(jwtTokenProvider.createAccessToken(member.getId(), member.getName(), member.getRole().getRoleType()), null));
        }
        if (!jwtTokenProvider.isExpired(accessToken)) {
            return new UserResponse(member.getMemberDto(), null);
        }
        throw new JwtException(JwtUserExceptionType.EXPIRED_JWT_TOKEN);
    }
}
