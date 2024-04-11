package com.duboribu.ecommerce.auth.service;

import com.duboribu.ecommerce.entity.member.Role;
import com.duboribu.ecommerce.enums.RoleType;
import com.duboribu.ecommerce.repository.RoleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleJpaRepository roleJpaRepository;
    @Transactional
    public Role findById(RoleType roleType) {
        return roleJpaRepository.findById(roleType.getDbValue())
                .orElseThrow(IllegalAccessError::new);
    }
    @Transactional
    public Role findByName(RoleType roleType) {
        return roleJpaRepository.findByRoleType(roleType)
                .orElseThrow(IllegalAccessError::new);
    }
}
