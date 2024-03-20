package com.duboribu.ecommerce.auth.repository;

import com.duboribu.ecommerce.entity.MemberToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberTokenJpaRepository extends JpaRepository<MemberToken, Long> {
}
