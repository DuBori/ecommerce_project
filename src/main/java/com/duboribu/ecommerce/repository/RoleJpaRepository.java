package com.duboribu.ecommerce.repository;

import com.duboribu.ecommerce.entity.member.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleJpaRepository extends JpaRepository<Role, Long> {
}
