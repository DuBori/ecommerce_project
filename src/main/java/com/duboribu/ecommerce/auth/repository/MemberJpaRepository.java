package com.duboribu.ecommerce.auth.repository;

import com.duboribu.ecommerce.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, String> {
    Optional<Member> findByName(String username);
}
