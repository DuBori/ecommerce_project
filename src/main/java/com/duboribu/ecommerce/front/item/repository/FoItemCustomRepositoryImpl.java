package com.duboribu.ecommerce.front.item.repository;

import com.duboribu.ecommerce.front.dto.response.FoItemResponse;
import com.duboribu.ecommerce.front.dto.response.QFoItemResponse;
import com.duboribu.ecommerce.front.item.dto.FoItemView;
import com.duboribu.ecommerce.front.item.dto.QFoItemView;
import com.duboribu.ecommerce.front.order.dto.CreateOrderRequest;
import com.duboribu.ecommerce.front.order.dto.FoOrderItemView;
import com.duboribu.ecommerce.front.order.dto.FoOrderResponse;
import com.duboribu.ecommerce.front.order.dto.QFoOrderItemView;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

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
    public Page<FoItemResponse> normalList(String category, Pageable pageable) {
        List<FoItemResponse> list = jpaQueryFactory.select(new QFoItemResponse(item.id, book.title, price.value, item.filePath))
                .from(item)
                .innerJoin(book)
                .on(item.id.eq(book.id))
                .innerJoin(price)
                .on(price.item.id.eq(item.id))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .where(isCategory(category))
                .fetch();
        JPAQuery<Long> totalCount = jpaQueryFactory.select(item.count())
                .from(item);
        return PageableExecutionUtils.getPage(list, pageable, totalCount::fetchOne);
    }

    private Predicate isCategory(String category) {
        if (StringUtils.hasText(category)) {
            return item.category.name.eq(category);
        }
        return Expressions.asBoolean(true).isTrue();
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

    @Override
    public FoOrderResponse itemViewResponses(CreateOrderRequest request) {
        List<FoOrderItemView> list = jpaQueryFactory.select(new QFoOrderItemView(book.id, book.title, book.author, book.publisher, book.filePath, price.value, stock.count, book.state, book.comment, book.information, book.weight))
                .from(book)
                .innerJoin(stock)
                .on(book.id.eq(stock.item.id))
                .innerJoin(price)
                .on(book.eq(price.item))
                .where(book.id.in(request.getProductIds()))
                .fetch();


        int totalPrice = 0;
        for (FoOrderItemView item : list) {
            for (CreateOrderRequest.OrderItemRequest orderItem : request.getOrderItemRequest()) {
                if (item.getId().equals(orderItem.getProductId())) {
                    item.mactchedUntiyPrice(orderItem.getQuantity());
                    break;
                }
            }
            totalPrice += item.getUnitPrice();
        }

        return new FoOrderResponse(getProductName(list), totalPrice, list);

    }

    private String getProductName(List<FoOrderItemView> list) {
        if (list.size() > 1) {
            return list.get(0).getTitle() + "외 " + (list.size() - 1);
        }
        return list.get(0).getTitle();
    }
}
