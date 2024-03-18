package com.duboribu.ecommerce.auth.service;

import com.duboribu.ecommerce.auth.repository.RoleJpaRepository;
import com.duboribu.ecommerce.entity.Role;
import com.duboribu.ecommerce.enums.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleJpaRepository roleJpaRepository;

    public Role findById(RoleType roleType) {
        return roleJpaRepository.findById(roleType.ROLE_USER.getDbValue())
                .orElseThrow(IllegalAccessError::new);
    }
}
