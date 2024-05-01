package com.duboribu.ecommerce.front.member.service;

import com.duboribu.ecommerce.front.main.domain.request.FoMyPageOrderResponse;
import com.duboribu.ecommerce.front.member.domain.response.MemberResponse;
import com.duboribu.ecommerce.front.order.repository.FoOrderCustomRepository;
import com.duboribu.ecommerce.repository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoMemberService {
    private final MemberJpaRepository memberJpaRepository;
    private final FoOrderCustomRepository foOrderCustomRepository;
    @Transactional
    public MemberResponse findMember(String userId) {
        return new MemberResponse(memberJpaRepository.getReferenceById(userId));
    }
    @Transactional
    public List<FoMyPageOrderResponse> findOrderListByUser(String userId) {
        return foOrderCustomRepository.findOrderListByUser(userId);
    }
}
