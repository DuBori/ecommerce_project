package com.duboribu.ecommerce.repository;

import com.duboribu.ecommerce.entity.member.Role;
import com.duboribu.ecommerce.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleJpaRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleType(RoleType roleType);
}
