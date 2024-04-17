package com.duboribu.ecommerce.admin.category.repository;

import com.duboribu.ecommerce.admin.category.dto.CategoryResponse;
import com.duboribu.ecommerce.admin.category.dto.QCategoryResponse;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.duboribu.ecommerce.entity.QCategory.category;
@Slf4j
@Repository
@RequiredArgsConstructor
public class CategoryCustomJpaRepositoryImpl implements CategoryCustomJpaRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<CategoryResponse> list(Pageable pageable) {
        // 부모 카테고리
        List<CategoryResponse> rootCategory = jpaQueryFactory.select(new QCategoryResponse(category.id, category.parent.id, category.name, category.code, category.state))
                .from(category)
                .where(category.parent.isNull())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        // 자식 카테고리
        List<CategoryResponse> childCategory = jpaQueryFactory.select(new QCategoryResponse(category.id, category.parent.id, category.name, category.code, category.state))
                .from(category)
                .where(category.parent.isNotNull())
                .fetch();

        for (CategoryResponse parent : rootCategory) {
            List<CategoryResponse> children = findChildren(parent, childCategory);
            parent.matchedList(children);
        }

        // 카운트쿼리
        JPAQuery<Long> countQuery = jpaQueryFactory.select(category.count())
                .from(category);

        return PageableExecutionUtils.getPage(rootCategory, pageable, countQuery::fetchOne);
    }

    private List<CategoryResponse> findChildren(CategoryResponse parent, List<CategoryResponse> childCategory) {
        List<CategoryResponse> children = new ArrayList<>();
        for (CategoryResponse child : childCategory) {
            log.debug("CategoryResponse :{}", child);
            if (child.getParentId().equals(parent.getId())) {
                List<CategoryResponse> grandChildren = findChildren(child, childCategory);
                child.matchedList(grandChildren);
                children.add(child);
            }
        }
        return children;
    }

    @Override
    public Optional<CategoryResponse> findById(Long id) {
        CategoryResponse categoryResponse = jpaQueryFactory.select(new QCategoryResponse(category.id, category.parent.id, category.name, category.code, category.state))
                .from(category)
                .where(category.id.eq(id))
                .fetchOne();
        if (categoryResponse != null) {
            return Optional.of(categoryResponse);
        }
        return Optional.empty();
    }
}
