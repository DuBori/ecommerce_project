package com.duboribu.ecommerce.auth.service;

import com.duboribu.ecommerce.auth.domain.UserDto;
import com.duboribu.ecommerce.auth.domain.response.JwtTokenResponse;
import com.duboribu.ecommerce.auth.domain.response.UserResponse;
import com.duboribu.ecommerce.auth.enums.UserType;
import com.duboribu.ecommerce.auth.social.SocialProfile;
import com.duboribu.ecommerce.auth.util.JwtTokenProvider;
import com.duboribu.ecommerce.entity.member.Member;
import com.duboribu.ecommerce.entity.member.MemberToken;
import com.duboribu.ecommerce.entity.member.Role;
import com.duboribu.ecommerce.enums.RoleType;
import com.duboribu.ecommerce.enums.SocialType;
import com.duboribu.ecommerce.repository.MemberJpaRepository;
import com.duboribu.ecommerce.repository.MemberTokenJpaRepository;
import com.duboribu.ecommerce.repository.RoleJpaRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final BCryptPasswordEncoder encoder;
    private final MemberJpaRepository memberJpaRepository;
    private final RoleJpaRepository roleJpaRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberTokenJpaRepository memberTokenJpaRepository;
    private final SocialAuthService socialAuthService;
    private final EntityManager em;
    @Transactional
    public UserResponse join(UserDto userDto) {
        log.info("JoinUsername : {}", userDto.getUsername());
        userDto.encodePassword(encoder.encode(userDto.getPassword()));
        if (memberJpaRepository.findById(userDto.getUsername()).isPresent()) {
            return null;
        }
        final Optional<Role> byRoleType = roleJpaRepository.findByRoleType(RoleType.ROLE_USER);
        if (byRoleType.isEmpty()) {
            return null;
        }
        final Role role = byRoleType.get();
        Member saveMember = memberJpaRepository.save(userDto.toEntity(role));
        em.flush();
        return new UserResponse(userDto, new JwtTokenResponse(jwtTokenProvider.createAccessToken(userDto), issueRefreshToken(saveMember)));
    }
    @Transactional
    public UserResponse joinAdmin(UserDto userDto) {

        if (memberJpaRepository.findById(userDto.getUsername()).isPresent()) {
            return null;
        }
        userDto.encodePassword(encoder.encode(userDto.getPassword()));
        final Optional<Role> byRoleType = roleJpaRepository.findByRoleType(RoleType.ROLE_ADMIN);
        if (byRoleType.isEmpty()) {
            return null;
        }
        log.info("amdin role : {}", byRoleType.get());
        Member saveMember = memberJpaRepository.save(userDto.toEntity(byRoleType.get()));
        em.flush();
        return new UserResponse(userDto, new JwtTokenResponse(jwtTokenProvider.createAccessToken(userDto), issueRefreshToken(saveMember)));
    }

    @Transactional
    public UserResponse login(UserDto userDto) {
        Optional<Member> findMember = memberJpaRepository.findById(userDto.getUsername());
        if (findMember.isEmpty()) {
            return null;
        }
        Member member = findMember.get();
        if (!encoder.matches(userDto.getPassword(), member.getPwd())) {
            return null;
        }
        String refreshToken = member.getMemberToken().getRefreshToken();
        if (jwtTokenProvider.isExpired(refreshToken)) {
            String createRefreshToken = jwtTokenProvider.createRefreshToken(member.getId());
            String accessToken = jwtTokenProvider.createAccessToken(member.getId(), member.getName(), member.getRole().getRoleType());
            return new UserResponse(userDto, new JwtTokenResponse(accessToken, createRefreshToken));
        }
        String accessToken = jwtTokenProvider.createAccessToken(member.getId(), member.getName(), member.getRole().getRoleType());
        UserDto memberDto = member.getMemberDto();
        return new UserResponse(memberDto, new JwtTokenResponse(accessToken, null));
    }

    @Transactional
    public UserResponse saveSocialUser(String code, SocialType site) {
        if (SocialType.ERROR.equals(site)) {
            return null;
        }
        try {
            SocialProfile socialProfile = null;
            if (SocialType.NAVER.equals(site)) {
                socialProfile = new SocialProfile(socialAuthService.naverProcess(code));
            } else if (SocialType.KAKAO.equals(site)) {
                socialProfile = new SocialProfile(socialAuthService.kakaoProcess(code));
            } else {
                socialProfile = new SocialProfile(socialAuthService.googleProcess(code));
            }

            Optional<Member> findMember = memberJpaRepository.findById(socialProfile.getEmail());

            if (findMember.isEmpty()) {
                return join(new UserDto(socialProfile, UserType.JOIN_MEMBER));
            }
            Member member = findMember.get();
            UserDto userDto = new UserDto(socialProfile, UserType.LOGIN_MEMBER);
            if (jwtTokenProvider.isExpired(member.getMemberToken().getRefreshToken())) {
                return new UserResponse(userDto, new JwtTokenResponse(jwtTokenProvider.createAccessToken(userDto), issueRefreshToken(member)));
            }
            String accessToken = jwtTokenProvider.createAccessToken(userDto);
            return new UserResponse(userDto, new JwtTokenResponse(accessToken, null));
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private String issueRefreshToken(Member member) {
        final String token = jwtTokenProvider.createRefreshToken(member.getId());
        if (member.getMemberToken() != null) {
            memberTokenJpaRepository.delete(member.getMemberToken());
        }
        member.updateToken(memberTokenJpaRepository.save(new MemberToken(token)));
        return token;
    }
}
