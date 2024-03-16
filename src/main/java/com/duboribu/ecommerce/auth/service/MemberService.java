package com.duboribu.ecommerce.auth.service;

import com.duboribu.ecommerce.auth.dao.MemberDao;
import com.duboribu.ecommerce.auth.domain.UserDto;
import com.duboribu.ecommerce.auth.repository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberDao memberDao;
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
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberJpaRepository.findById(username)
                .orElseThrow(() -> {throw new IllegalArgumentException("회원이 아닙니다");});
    }
}
