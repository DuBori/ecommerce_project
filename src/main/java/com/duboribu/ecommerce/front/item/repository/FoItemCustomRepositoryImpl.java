package com.duboribu.ecommerce.front.item.repository;

import com.duboribu.ecommerce.front.dto.response.FoItemResponse;
import com.duboribu.ecommerce.front.dto.response.QFoItemResponse;
import com.duboribu.ecommerce.front.item.service.FoItemView;
import com.duboribu.ecommerce.front.item.service.QFoItemView;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.duboribu.ecommerce.entity.QBook.book;
import static com.duboribu.ecommerce.entity.QItem.item;
import static com.duboribu.ecommerce.entity.QPrice.price;
import static com.duboribu.ecommerce.entity.QStock.stock;

@Repository
@RequiredArgsConstructor
public class FoItemCustomRepositoryImpl implements FoItemCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<FoItemResponse> normalList(Pageable pageable) {
        List<FoItemResponse> list = jpaQueryFactory.select(new QFoItemResponse(item.id, book.title, price.value, item.filePath))
                .from(item)
                .innerJoin(book)
                .on(item.id.eq(book.id))
                .innerJoin(price)
                .on(price.item.id.eq(item.id))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        JPAQuery<Long> totalCount = jpaQueryFactory.select(item.count())
                .from(item);
        return PageableExecutionUtils.getPage(list, pageable, totalCount::fetchOne);
    }

    @Override
    public FoItemView loadItemViewResponse(Long itemId) {
        return jpaQueryFactory.select(new QFoItemView(book.id, book.title, book.author, book.publisher, book.filePath, price.value, stock.count, book.state, book.comment, book.information, book.weight))
                .from(book)
                .innerJoin(stock)
                .on(book.id.eq(stock.item.id))
                .innerJoin(price)
                .on(book.eq(price.item))
                .where(book.id.eq(itemId))
                .fetchOne();
    }
}
