package com.duboribu.ecommerce.auth.service;

import com.duboribu.ecommerce.auth.domain.UserDto;
import com.duboribu.ecommerce.auth.domain.response.UserResponse;
import com.duboribu.ecommerce.auth.repository.MemberJpaRepository;
import com.duboribu.ecommerce.auth.util.JwtTokenProvider;
import com.duboribu.ecommerce.entity.Member;
import com.duboribu.ecommerce.entity.MemberToken;
import com.duboribu.ecommerce.entity.PrincipalDetails;
import com.duboribu.ecommerce.entity.Role;
import com.duboribu.ecommerce.enums.RoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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


    @Transactional
    public UserResponse join(UserDto userDto) {
        if (!memberJpaRepository.findById(userDto.getUsername()).isEmpty()) {
            return null;
        }
        encodePassword(userDto);
        final Role user = roleService.findById(RoleType.ROLE_USER);
        memberJpaRepository.save(userDto.toEntity(user));
        return new UserResponse(userDto);
    }
    private void encodePassword(UserDto userDto) {
        userDto.setPassword(encoder.encode(userDto.getPassword()));
    }

    public Member findByMemberName(String name) {
        Member member = memberJpaRepository.findByName(name)
                .orElse(null);
        return member;
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

    public String issueRefreshToken(Member member) {
        final String token = jwtTokenProvider.createRefreshToken();
        memberTokenService.delete(member.getMemberToken());
        member.updateToken(new MemberToken(token));
        return token;
    }
}
