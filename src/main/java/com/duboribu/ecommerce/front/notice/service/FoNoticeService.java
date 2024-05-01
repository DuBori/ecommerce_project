package com.duboribu.ecommerce.front.notice.service;

import com.duboribu.ecommerce.entity.Comment;
import com.duboribu.ecommerce.entity.Notice;
import com.duboribu.ecommerce.entity.member.Member;
import com.duboribu.ecommerce.front.notice.dto.reponse.FoNoticeResponse;
import com.duboribu.ecommerce.front.notice.dto.request.CreateCommentReq;
import com.duboribu.ecommerce.front.notice.repository.FoNoticeCustomJpaRepository;
import com.duboribu.ecommerce.repository.CommentJpaRepository;
import com.duboribu.ecommerce.repository.MemberJpaRepository;
import com.duboribu.ecommerce.repository.NoticeJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FoNoticeService {
    private final NoticeJpaRepository noticeJpaRepository;
    private final CommentJpaRepository commentJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    
    private final FoNoticeCustomJpaRepository foNoticeCustomJpaRepository;

    @Transactional
    public Page<FoNoticeResponse> list(PageRequest request) {
        return foNoticeCustomJpaRepository.getFoNoticeList(request);
    }
    @Transactional
    public FoNoticeResponse noticeView(Long id) {
        return foNoticeCustomJpaRepository.noticeView(id);
    }
    @Transactional
    public boolean addComment(CreateCommentReq createCommentReq, String userId) {
        Optional<Notice> findNotice = noticeJpaRepository.findById(createCommentReq.getId());
        if (findNotice.isEmpty()) {
            throw new IllegalArgumentException("해당하는 공지가 없습니다.");
        }
        Optional<Member> findMember = memberJpaRepository.findById(userId);
        if (findMember.isEmpty()) {
            throw new IllegalArgumentException("해당하는 회원이 없습니다.");
        }
        Notice notice = findNotice.get();
        // 대댓글인 경우
        if (createCommentReq.getParentId() != null && createCommentReq.getParentId() > 0) {
            Comment parentComment = commentJpaRepository.findById(createCommentReq.getParentId()).get();
            notice.getCommentList().add(new Comment(notice, findMember.get(), createCommentReq, parentComment));
            return true;
        }
        notice.getCommentList().add( new Comment(notice, findMember.get(), createCommentReq));
        return true;
    }
}
