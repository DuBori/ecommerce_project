package com.duboribu.ecommerce.admin.member.service;

import com.duboribu.ecommerce.admin.common.BoSearchCommand;
import com.duboribu.ecommerce.admin.main.dto.response.BoDashRes;
import com.duboribu.ecommerce.admin.member.dto.request.UpdateMemberState;
import com.duboribu.ecommerce.admin.member.dto.response.AdminMemberResponse;
import com.duboribu.ecommerce.admin.member.repository.AdminMemberCustomJpaRepository;
import com.duboribu.ecommerce.entity.member.Member;
import com.duboribu.ecommerce.repository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoMemberService {
    private final AdminMemberCustomJpaRepository adminMemberCustomJpaRepository;
    private final MemberJpaRepository memberJpaRepository;

    @Transactional
    public Page<AdminMemberResponse> list(BoSearchCommand search) {
        return adminMemberCustomJpaRepository.list(search, PageRequest.of(search.getPage(), search.getSize()));
    }
    @Transactional
    public AdminMemberResponse getMemberById(String userId) {
        return adminMemberCustomJpaRepository.getMemberById(userId);
    }
    @Transactional
    public void updateMemberState(UpdateMemberState updateMemberState) {
        if (updateMemberState.validate()) {
            throw new IllegalArgumentException("필수값을 넣지 않았습니다");
        }
        Optional<Member> findMember = memberJpaRepository.findById(updateMemberState.getUserId());
        if (findMember.isEmpty()) {
            throw new IllegalArgumentException("해당하는 회원이 존재하지 않습니다.");
        }

        Member member = findMember.get();
        member.updateState(updateMemberState.getState());
    }
    @Transactional
    public BoDashRes count() {
        long totalCount = memberJpaRepository.count();
        LocalDateTime startOfYear = LocalDateTime.now().withDayOfYear(1);
        LocalDateTime endOfYear = LocalDateTime.now().withDayOfYear(365);
        long currentCount = memberJpaRepository.countByCreatedAtBetween(startOfYear, endOfYear);
        LocalDateTime startOfLastYear = LocalDateTime.now().minusYears(1).withDayOfYear(1);
        LocalDateTime endOfLastYear = LocalDateTime.now().minusYears(1).withDayOfYear(365); // 혹은 366
        long lastYearCount = memberJpaRepository.countByCreatedAtBetween(startOfLastYear, endOfLastYear);

        return new BoDashRes(totalCount, currentCount, lastYearCount);
    }
}
