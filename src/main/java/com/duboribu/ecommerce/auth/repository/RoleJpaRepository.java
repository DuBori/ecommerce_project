package com.duboribu.ecommerce.auth.repository;

import com.duboribu.ecommerce.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleJpaRepository extends JpaRepository<Role, Long> {
}
