package com.duboribu.ecommerce.admin.order.repository;

import com.duboribu.ecommerce.admin.order.dto.AdminOrderView;
import com.duboribu.ecommerce.admin.order.dto.QAdminOrderView;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.duboribu.ecommerce.entity.QOrder.order;
import static com.duboribu.ecommerce.entity.member.QMember.member;

@Repository
@RequiredArgsConstructor
public class AdminOrderCustomRepositoryImpl implements AdminOrderCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public Page<AdminOrderView> list(Pageable pageable) {
        List<AdminOrderView> list = jpaQueryFactory.select(new QAdminOrderView(order.id, order.member.name, order.state))
                .from(order)
                .leftJoin(member)
                .on(order.member.eq(member))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        JPAQuery<Long> countQuery = jpaQueryFactory.select(order.count())
                .from(order);
        return PageableExecutionUtils.getPage(list, pageable, countQuery::fetchCount);
    }
}
