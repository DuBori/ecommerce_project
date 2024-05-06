package com.duboribu.ecommerce.front.notice.repository;

import com.duboribu.ecommerce.entity.Notice;
import com.duboribu.ecommerce.front.notice.dto.reponse.FoNoticeComment;
import com.duboribu.ecommerce.front.notice.dto.reponse.FoNoticeResponse;
import com.duboribu.ecommerce.front.notice.dto.reponse.QFoNoticeComment;
import com.duboribu.ecommerce.front.notice.dto.reponse.QFoNoticeResponse;
import com.duboribu.ecommerce.front.qna.dto.request.NoticeRequest;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.duboribu.ecommerce.entity.QComment.comment;
import static com.duboribu.ecommerce.entity.QNotice.notice;
import static com.duboribu.ecommerce.entity.member.QMember.member;
@Slf4j
@Repository
@RequiredArgsConstructor
public class FoNoticeCustomJpaRepositoryImpl implements FoNoticeCustomJpaRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<FoNoticeResponse> getFoNoticeList(Pageable pageable, NoticeRequest noticeRequest) {
        List<Notice> noticeList = jpaQueryFactory.select(notice)
                .from(notice)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .where(notice.noticeType.eq(noticeRequest.getNoticeType()),
                        isNotNullUserId(noticeRequest.getUserId()))
                .fetch();
        List<FoNoticeResponse> list = new ArrayList<>();
        noticeList.forEach(
                it -> list.add(new FoNoticeResponse(
                        it.getId(),
                        it.getTitle(),
                        it.getComment(),
                        it.getFilePath(),
                        getStringDate(it.getCreatedAt()),
                        getStringDate(it.getUpdatedAt()),
                        it.getCreatedBy(),
                        null
                ))
        );

        JPAQuery<Long> countQuery = jpaQueryFactory.select(notice.count())
                .from(notice);

        return PageableExecutionUtils.getPage(list, pageable, countQuery::fetchCount);
    }

    private BooleanExpression isNotNullUserId(String userId) {
        if (StringUtils.hasText(userId)) {
            return notice.member.id.eq(userId);
        }
        return null;
    }

    @Override
    public FoNoticeResponse noticeView(Long id) {
        // 게시글
        FoNoticeResponse foNoticeResponse = jpaQueryFactory.select(new QFoNoticeResponse(notice.id,
                        notice.title,
                        notice.comment,
                        notice.filePath,
                        notice.createdAt,
                        notice.updatedAt,
                        notice.createdBy
                ))
                .from(notice)
                .where(notice.id.eq(id))
                .orderBy(notice.createdAt.desc())
                .fetchOne();

        // 부모 댓글
        List<FoNoticeComment> parentComments = jpaQueryFactory.select(new QFoNoticeComment(comment.id, comment.parentComment.id, member.name, comment.detail))
                .from(comment)
                .innerJoin(member)
                .on(comment.member.eq(member))
                .where(comment.notice.id.eq(id), comment.parentComment.isNull())
                .orderBy(comment.createdAt.desc())
                .fetch();

        //자식댓글
        List<FoNoticeComment> childComments = jpaQueryFactory.select(new QFoNoticeComment(comment.id, comment.parentComment.id, member.name, comment.detail))
                .from(comment)
                .innerJoin(member)
                .on(comment.member.eq(member))
                .where(comment.notice.id.eq(id), comment.parentComment.isNotNull())
                .orderBy(comment.createdAt.desc())
                .fetch();

        for (FoNoticeComment parent : parentComments) {
            List<FoNoticeComment> children = findChildren(parent, childComments);
            parent.matchedList(children);
        }

        foNoticeResponse.matchedComments(parentComments);

        return foNoticeResponse;
    }

    private List<FoNoticeComment> findChildren(FoNoticeComment parent, List<FoNoticeComment> childComment) {
        List<FoNoticeComment> children = new ArrayList<>();
        for (FoNoticeComment child : childComment) {
            if (child.getParentCommentId().equals(parent.getCommentId())) {
                List<FoNoticeComment> grandChildren = findChildren(child, childComment);
                child.matchedList(grandChildren);
                children.add(child);
            }
        }
        return children;
    }


    private String getStringDate(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return localDateTime.format(formatter);
    }

}
