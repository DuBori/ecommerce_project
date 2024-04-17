package com.duboribu.ecommerce.front.category.repository;

import com.duboribu.ecommerce.front.category.dto.FoCategoryResponse;
import com.duboribu.ecommerce.front.category.dto.QFoCategoryResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.duboribu.ecommerce.entity.QCategory.category;

@Repository
@RequiredArgsConstructor
public class FoCategoryCustomJpaRepositoryImpl implements FoCategoryCustomJpaRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<FoCategoryResponse> list(String book) {
        return jpaQueryFactory.select(new QFoCategoryResponse(category.id, category.name, category.code))
                .from(category)
                .where(category.state.eq("Y"),
                        category.parent.code.eq(book))
                .fetch();
    }
}
