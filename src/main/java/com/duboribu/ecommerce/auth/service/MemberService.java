package com.duboribu.ecommerce.auth.service;

import com.duboribu.ecommerce.auth.domain.UserDto;
import com.duboribu.ecommerce.auth.util.JwtTokenProvider;
import com.duboribu.ecommerce.entity.member.Member;
import com.duboribu.ecommerce.entity.member.PrincipalDetails;
import com.duboribu.ecommerce.repository.MemberJpaRepository;
import jakarta.persistence.EntityManager;
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
    private final EntityManager em;




    private void encodePassword(UserDto userDto) {
        userDto.setPassword(encoder.encode(userDto.getPassword()));
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


    public Optional<Member> findById(String username) {
        return memberJpaRepository.findById(username);
    }
}
