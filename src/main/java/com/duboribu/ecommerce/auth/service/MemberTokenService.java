package com.duboribu.ecommerce.auth.service;

import com.duboribu.ecommerce.auth.repository.MemberTokenJpaRepository;
import com.duboribu.ecommerce.entity.MemberToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberTokenService {
    private final MemberTokenJpaRepository memberTokenJpaRepository;
    public void delete(MemberToken memberToken) {
        memberTokenJpaRepository.delete(memberToken);
    }

    public MemberToken save(MemberToken memberToken) {
        return memberTokenJpaRepository.save(memberToken);
    }
}
