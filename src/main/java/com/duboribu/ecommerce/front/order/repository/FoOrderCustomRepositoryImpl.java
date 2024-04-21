package com.duboribu.ecommerce.front.order.repository;

import com.duboribu.ecommerce.entity.Order;
import com.duboribu.ecommerce.front.order.dto.FoOrderItemResponse;
import com.duboribu.ecommerce.front.order.dto.FoPaymentView;
import com.duboribu.ecommerce.front.order.dto.QFoOrderItemResponse;
import com.duboribu.ecommerce.front.order.dto.QFoPaymentView;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.duboribu.ecommerce.entity.QBook.book;
import static com.duboribu.ecommerce.entity.QItem.item;
import static com.duboribu.ecommerce.entity.QOrder.order;
import static com.duboribu.ecommerce.entity.QOrderItem.orderItem;
import static com.duboribu.ecommerce.entity.QPayment.payment;

@Repository
@RequiredArgsConstructor
public class FoOrderCustomRepositoryImpl implements FoOrderCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public FoPaymentView findById(String id) {
        FoPaymentView foPaymentView = jpaQueryFactory.select(new QFoPaymentView(order.id))
                .from(payment)
                .innerJoin(order)
                .on(payment.order.eq(order))
                .where(payment.merchant_uid.eq(id))
                .fetchOne();

        Order findOrder = jpaQueryFactory.select(order)
                .from(payment)
                .innerJoin(order)
                .on(payment.order.eq(order))
                .where(payment.merchant_uid.eq(id))
                .fetchOne();

        List<FoOrderItemResponse> list = jpaQueryFactory.select(new QFoOrderItemResponse(item.id, book.title, orderItem.netPrice, orderItem.count))
                .from(payment)
                .innerJoin(order)
                .on(payment.order.eq(order))
                .innerJoin(orderItem)
                .on(order.eq(orderItem.order))
                .innerJoin(item)
                .on(orderItem.item.eq(item))
                .innerJoin(book)
                .on(book.id.eq(item.id))
                .where(payment.merchant_uid.eq(id))
                .fetch();

        foPaymentView.mactedOrderListAndTotalPrice(findOrder.getTotalPrice(), list);

        return foPaymentView;
    }
}
