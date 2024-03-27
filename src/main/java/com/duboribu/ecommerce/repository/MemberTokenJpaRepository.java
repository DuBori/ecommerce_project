package com.duboribu.ecommerce.repository;

import com.duboribu.ecommerce.entity.member.MemberToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberTokenJpaRepository extends JpaRepository<MemberToken, Long> {
}
