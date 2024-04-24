package com.duboribu.ecommerce.admin.item.repository;

import com.duboribu.ecommerce.admin.item.dto.QResponseBook;
import com.duboribu.ecommerce.admin.item.dto.ResponseBook;
import com.duboribu.ecommerce.admin.item.dto.SearchItemRequest;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

import static com.duboribu.ecommerce.entity.QBook.book;
import static com.duboribu.ecommerce.entity.QCategory.category;
import static com.duboribu.ecommerce.entity.QPrice.price;

@Repository
@RequiredArgsConstructor
public class AdminItemCustomRepositoryImpl implements AdminItemCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public ResponseBook findByBookId(Long id) {
        return jpaQueryFactory.select(new QResponseBook(book.id, book.title, book.author, book.publisher, book.filePath, book.price, category.id))
                .from(book)
                .innerJoin(price)
                .on(book.id.eq(price.item.id))
                .innerJoin(category)
                .on(book.category.eq(category))
                .where(book.id.eq(id))
                .fetchOne();
    }

    @Override
    public Page<ResponseBook> list(SearchItemRequest searchItemRequest, Pageable pageable) {
        QueryResults<ResponseBook> results = jpaQueryFactory.select(new QResponseBook(book.id, book.title, book.author, book.publisher, book.filePath, book.price, category.id))
                .from(book)
                .innerJoin(price)
                .on(book.id.eq(price.item.id))
                .where(search(searchItemRequest))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }
    private BooleanExpression search(SearchItemRequest searchItemRequest) {
        if ("title".equals(searchItemRequest.getSearchType())) {
            return book.title.like("%" + searchItemRequest.getKeyword() + "%");
        }
        if ("author".equals(searchItemRequest.getSearchType())) {
            return book.author.like("%" + searchItemRequest.getKeyword() + "%");
        }
        if ("price".equals(searchItemRequest.getSearchType()) && searchItemRequest.getKeyword().matches("\\d+")) {
            return price.value.eq(new BigDecimal(searchItemRequest.getKeyword()));
        }
        return null;
    }
}
