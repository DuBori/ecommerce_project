package com.duboribu.ecommerce.admin.notice.repository;

import com.duboribu.ecommerce.admin.notice.dto.response.BoNoticeRes;
import com.duboribu.ecommerce.admin.notice.dto.response.QBoNoticeRes;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.duboribu.ecommerce.entity.QNotice.notice;

@Repository
@RequiredArgsConstructor
public class AdminNoticeCustomRepositoryImpl implements AdminNoticeCustomRepository {
    
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<BoNoticeRes> findNoticeList(Pageable pageable, String searchType, String keyword) {
        List<BoNoticeRes> list = jpaQueryFactory
                .select(new QBoNoticeRes(
                        notice.id,
                        notice.title,
                        notice.comment,
                        notice.state,
                        notice.filePath,
                        notice.noticeType,
                        notice.createdBy,
                        notice.createdAt,
                        notice.updatedAt
                ))
                .from(notice)
                .where(searchCondition(searchType, keyword))
                .orderBy(notice.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(notice.count())
                .from(notice)
                .where(searchCondition(searchType, keyword));

        return PageableExecutionUtils.getPage(list, pageable, countQuery::fetchOne);
    }

    @Override
    public BoNoticeRes findNoticeById(Long id) {
        return jpaQueryFactory
                .select(new QBoNoticeRes(
                        notice.id,
                        notice.title,
                        notice.comment,
                        notice.state,
                        notice.filePath,
                        notice.noticeType,
                        notice.createdBy,
                        notice.createdAt,
                        notice.updatedAt
                ))
                .from(notice)
                .where(notice.id.eq(id))
                .fetchOne();
    }

    private BooleanExpression searchCondition(String searchType, String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return null;
        }
        if ("title".equals(searchType)) {
            return notice.title.contains(keyword);
        }
        if ("comment".equals(searchType)) {
            return notice.comment.contains(keyword);
        }
        return null;
    }
}
