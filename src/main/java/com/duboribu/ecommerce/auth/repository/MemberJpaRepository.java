package com.duboribu.ecommerce.auth.repository;

import com.duboribu.ecommerce.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface MemberJpaRepository extends JpaRepository<Member, String> {
    Member findByName(String username);
}
