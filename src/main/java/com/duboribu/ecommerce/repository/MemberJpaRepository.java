package com.duboribu.ecommerce.repository;

import com.duboribu.ecommerce.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, String> {
    Optional<Member> findByName(String username);
    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
