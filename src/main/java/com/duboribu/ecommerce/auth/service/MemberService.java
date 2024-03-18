package com.duboribu.ecommerce.auth.service;

import com.duboribu.ecommerce.auth.domain.UserDto;
import com.duboribu.ecommerce.auth.repository.MemberJpaRepository;
import com.duboribu.ecommerce.entity.Member;
import com.duboribu.ecommerce.entity.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService implements UserDetailsService {
    private final BCryptPasswordEncoder encoder;

    private final MemberJpaRepository memberJpaRepository;


    @Transactional
    public UserDto join(UserDto user) {
        encodePassword(user);
        return new UserDto(memberJpaRepository.save(user.toEntity()));
    }
    private void encodePassword(UserDto userDto) {
        userDto.setPassword(encoder.encode(userDto.getPassword()));
    }

    public Member findByMemberName(String name) {
        Member member = memberJpaRepository.findByName(name)
                .orElse(null);
        return member;
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
}
