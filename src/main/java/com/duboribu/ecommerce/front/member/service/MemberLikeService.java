package com.duboribu.ecommerce.front.member.service;

import com.duboribu.ecommerce.auth.domain.DefaultResponse;
import com.duboribu.ecommerce.entity.Item;
import com.duboribu.ecommerce.entity.MemberLike;
import com.duboribu.ecommerce.entity.member.Member;
import com.duboribu.ecommerce.front.member.domain.request.LikeReq;
import com.duboribu.ecommerce.repository.ItemJpaRepository;
import com.duboribu.ecommerce.repository.MemberJpaRepository;
import com.duboribu.ecommerce.repository.MemberLikeJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberLikeService {
    private final MemberLikeJpaRepository memberLikeJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final ItemJpaRepository itemJpaRepository;
    @Transactional
    public DefaultResponse clickLike(LikeReq likeReq, String userId) {
        Optional<MemberLike> findMemberLike = memberLikeJpaRepository.findMemberLikeByItem_IdAndMember_Id(likeReq.getItemId(), userId);
        if (findMemberLike.isPresent()) {
            memberLikeJpaRepository.delete(findMemberLike.get());
            return new DefaultResponse("찜 해제 하였습니다", 201);
        }
        if (likeReq.isEmpty()) {
            throw new IllegalArgumentException("상품 아이디가 존재하지 않습니다");
        }
        Optional<Item> findItem = itemJpaRepository.findById(likeReq.getItemId());
        Optional<Member> findMember = memberJpaRepository.findById(userId);
        if (findItem.isEmpty() || findMember.isEmpty()) {
            throw new IllegalArgumentException("매칭되는 상품 아이디 및 회원이 존재하지 않습니다");
        }
        memberLikeJpaRepository.save(new MemberLike(findItem.get(), findMember.get()));
        return new DefaultResponse("찜 등록 하였습니다", 202);
    }

}
