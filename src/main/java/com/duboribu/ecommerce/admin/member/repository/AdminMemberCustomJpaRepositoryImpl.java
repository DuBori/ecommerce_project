package com.duboribu.ecommerce.admin.member.repository;

import com.duboribu.ecommerce.admin.common.BoSearchCommand;
import com.duboribu.ecommerce.admin.member.dto.response.AdminMemberResponse;
import com.duboribu.ecommerce.admin.member.dto.response.QAdminMemberResponse;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.duboribu.ecommerce.entity.member.QMember.member;
import static com.duboribu.ecommerce.entity.member.QRole.role;


@Repository
@RequiredArgsConstructor
public class AdminMemberCustomJpaRepositoryImpl implements AdminMemberCustomJpaRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<AdminMemberResponse> list(BoSearchCommand search, Pageable pageable) {
        List<AdminMemberResponse> list = jpaQueryFactory.select(new QAdminMemberResponse(member.id, member.name, role.roleType, member.state))
                .from(member)
                .innerJoin(role)
                .on(member.role.eq(role))
                .fetch();
        JPAQuery<Long> totalQuery = jpaQueryFactory.select(member.count())
                .from(member);

        return PageableExecutionUtils.getPage(list, pageable, totalQuery::fetchOne);
    }

    @Override
    public AdminMemberResponse getMemberById(String userId) {
        AdminMemberResponse adminMemberResponse = jpaQueryFactory.select(new QAdminMemberResponse(member.id, member.name, role.roleType, member.state))
                .from(member)
                .innerJoin(role)
                .on(member.role.eq(role))
                .where(member.id.eq(userId))
                .fetchOne();
        return adminMemberResponse;
    }
}
