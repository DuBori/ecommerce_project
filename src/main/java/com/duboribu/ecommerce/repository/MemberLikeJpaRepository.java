package com.duboribu.ecommerce.repository;

import com.duboribu.ecommerce.entity.MemberLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberLikeJpaRepository extends JpaRepository<MemberLike, Long> {
    Optional<MemberLike> findMemberLikeByItem_IdAndMember_Id(Long itemId, String userId);
}
