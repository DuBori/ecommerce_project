package com.duboribu.ecommerce.auth.service;

import com.duboribu.ecommerce.Utils.RestUtil;
import com.duboribu.ecommerce.auth.domain.UserDto;
import com.duboribu.ecommerce.auth.domain.response.JwtTokenResponse;
import com.duboribu.ecommerce.auth.domain.response.UserResponse;
import com.duboribu.ecommerce.auth.enums.UserType;
import com.duboribu.ecommerce.auth.repository.MemberJpaRepository;
import com.duboribu.ecommerce.auth.social.GooGleProfile;
import com.duboribu.ecommerce.auth.social.SocialProfile;
import com.duboribu.ecommerce.auth.social.kakao.dto.request.KaKaoTokenRequst;
import com.duboribu.ecommerce.auth.social.kakao.dto.response.KakaoProfile;
import com.duboribu.ecommerce.auth.social.kakao.dto.response.KakaoToken;
import com.duboribu.ecommerce.auth.social.naver.request.NaverTokenRequest;
import com.duboribu.ecommerce.auth.social.naver.response.NaverProfile;
import com.duboribu.ecommerce.auth.social.naver.response.NaverToken;
import com.duboribu.ecommerce.auth.util.JwtTokenProvider;
import com.duboribu.ecommerce.entity.Member;
import com.duboribu.ecommerce.entity.MemberToken;
import com.duboribu.ecommerce.entity.PrincipalDetails;
import com.duboribu.ecommerce.entity.Role;
import com.duboribu.ecommerce.enums.RoleType;
import com.duboribu.ecommerce.enums.SocialType;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService implements UserDetailsService {
    private final BCryptPasswordEncoder encoder;
    private final MemberJpaRepository memberJpaRepository;
    private final RoleService roleService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberTokenService memberTokenService;
    private final EntityManager em;
    private final String NaverRedirectUri = "http://localhost:8880/login/oauth2/code/naver";
    private final String NaverAccessTokenUri = "https://nid.naver.com/oauth2.0/token";
    private final String NaverUserInfoUri = "https://openapi.naver.com/v1/nid/me";

    @Transactional
    public UserResponse join(UserDto userDto) {
        if (!memberJpaRepository.findById(userDto.getUsername()).isEmpty()) {
            return null;
        }
        encodePassword(userDto);
        final Role user = roleService.findById(RoleType.ROLE_USER);
        Member saveMember = memberJpaRepository.save(userDto.toEntity(user));
        em.flush();
        return new UserResponse(userDto, new JwtTokenResponse(issueRefreshToken(saveMember), jwtTokenProvider.createAccessToken(userDto)));
    }
    private void encodePassword(UserDto userDto) {
        userDto.setPassword(encoder.encode(userDto.getPassword()));
    }

    public Optional<Member> findById(String id) {
        return memberJpaRepository.findById(id);
    }
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberJpaRepository.findById(username)
                .orElseThrow(() -> {
                    throw new UsernameNotFoundException("회원이 아닙니다");
                });
        return new PrincipalDetails(member);
    }

    private String issueRefreshToken(Member member) {
        final String token = jwtTokenProvider.createRefreshToken(member.getId());
        if (member.getMemberToken() != null) {
            memberTokenService.delete(member.getMemberToken());
        }
        member.updateToken(memberTokenService.save(new MemberToken(token)));
        return token;
    }
    @Transactional
    public UserResponse saveSocialUser(String code, SocialType site) {
        if (SocialType.ERROR.equals(site)) {
            return null;
        }
        try {
            SocialProfile socialProfile = null;
            if (SocialType.NAVER.equals(site)) {
                socialProfile = new SocialProfile(naverProcess(code));
            } else if (SocialType.KAKAO.equals(site)) {
                socialProfile = new SocialProfile(kakaoProcess(code));
            } else {
                socialProfile = new SocialProfile(googleProcess(code));
            }

            Optional<Member> findMember = findById(socialProfile.getEmail());

            if (findMember.isEmpty()) {
                return join(new UserDto(socialProfile, UserType.JOIN_MEMBER));
            }
            Member member = findMember.get();
            UserDto userDto = new UserDto(socialProfile, UserType.LOGIN_MEMBER);
            if (!jwtTokenProvider.validateToken(member.getMemberToken().getRefreshToken())) {
                return new UserResponse(userDto, new JwtTokenResponse(jwtTokenProvider.createAccessToken(userDto), issueRefreshToken(member)));
            }
            String accessToken = jwtTokenProvider.createAccessToken(member.getMemberToken().getRefreshToken());
            return new UserResponse(userDto, new JwtTokenResponse(accessToken, null));
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private NaverProfile naverProcess(String code) {
        NaverToken userToken = getUserTokenByNaverCode(code);
        NaverProfile naverProfile = findNaverProfile(userToken.getAccess_token());
        return naverProfile;
    }

    private NaverToken getUserTokenByNaverCode(String code) {
        NaverTokenRequest kaKaoRequestProfile = new NaverTokenRequest("fjEP_mtQ7ytXMesU8cpA", NaverRedirectUri, "DBycsiFFZN", code);
        ResponseEntity<NaverToken> response = RestUtil.post(NaverAccessTokenUri, kaKaoRequestProfile,  new HttpHeaders(), NaverToken.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        throw new IllegalArgumentException();
    }
    private NaverProfile findNaverProfile(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        ResponseEntity<NaverProfile> response = RestUtil.get(NaverUserInfoUri, headers, NaverProfile.class);
        if (response.getStatusCode() == HttpStatus.OK && "success".equals(response.getBody().getMessage())) {
            return response.getBody();
        }
        throw new IllegalArgumentException();
    }

    private KakaoProfile kakaoProcess(String code) {
        KakaoToken kakaoToken = getKaKaoTokenByCode(code);
        log.info("{}", kakaoToken);
        KakaoProfile profile = findProfile(kakaoToken.getAccess_token());
        log.info("{}", profile);
        return profile;
    }

    private KakaoToken getKaKaoTokenByCode(String code) {
        KaKaoTokenRequst kaKaoTokenRequst = new KaKaoTokenRequst("20eaea6eeefe7c1565e9b78ae5ba3537", "http://localhost:8080/auth/oauth2/code/kakao", code);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        ResponseEntity<KakaoToken> response = RestUtil.post("https://kauth.kakao.com/oauth/token", kaKaoTokenRequst, headers, KakaoToken.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        throw new IllegalArgumentException();
    }
    public KakaoProfile findProfile(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        ResponseEntity<KakaoProfile> response = RestUtil.get("https://kapi.kakao.com/v2/user/me", headers, KakaoProfile.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        throw new IllegalArgumentException();
    }

    private GooGleProfile googleProcess(String code) {
        return new GooGleProfile(code);
    }
}
