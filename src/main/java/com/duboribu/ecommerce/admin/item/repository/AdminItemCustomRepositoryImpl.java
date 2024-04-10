package com.duboribu.ecommerce.admin.item.repository;

import com.duboribu.ecommerce.admin.item.dto.QResponseBook;
import com.duboribu.ecommerce.admin.item.dto.ResponseBook;
import com.duboribu.ecommerce.admin.item.dto.SearchItemRequest;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.duboribu.ecommerce.entity.QBook.book;
import static com.duboribu.ecommerce.entity.QPrice.price;

@Repository
@RequiredArgsConstructor
public class AdminItemCustomRepositoryImpl implements AdminItemCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public ResponseBook findByBookId(Long id) {
        return jpaQueryFactory.select(new QResponseBook(book.id, book.title, book.author, book.publisher, price.value.intValue()))
                .from(book)
                .innerJoin(price)
                .on(book.id.eq(price.item.id))
                .where(book.id.eq(id))
                .fetchOne();
    }

    @Override
    public List<ResponseBook> list(SearchItemRequest searchItemRequest, Pageable pageable) {
        return jpaQueryFactory.select(new QResponseBook(book.id, book.title, book.author, book.publisher, price.value.intValue()))
                .from(book)
                .innerJoin(price)
                .on(book.id.eq(price.item.id))
                .fetch();
    }
}
