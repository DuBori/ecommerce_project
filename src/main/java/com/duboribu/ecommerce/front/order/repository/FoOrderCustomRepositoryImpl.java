package com.duboribu.ecommerce.front.order.repository;

import com.duboribu.ecommerce.entity.Order;
import com.duboribu.ecommerce.front.main.domain.reponse.FoMyPageOrderIdState;
import com.duboribu.ecommerce.front.main.domain.reponse.FoMyPageOrderItem;
import com.duboribu.ecommerce.front.main.domain.reponse.QFoMyPageOrderIdState;
import com.duboribu.ecommerce.front.main.domain.reponse.QFoMyPageOrderItem;
import com.duboribu.ecommerce.front.main.domain.request.FoMyPageOrderResponse;
import com.duboribu.ecommerce.front.order.dto.FoPaymentView;
import com.duboribu.ecommerce.front.order.dto.FoReceiptOrderItem;
import com.duboribu.ecommerce.front.order.dto.QFoPaymentView;
import com.duboribu.ecommerce.front.order.dto.QFoReceiptOrderItem;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.duboribu.ecommerce.entity.QBook.book;
import static com.duboribu.ecommerce.entity.QDelivery.delivery;
import static com.duboribu.ecommerce.entity.QItem.item;
import static com.duboribu.ecommerce.entity.QOrder.order;
import static com.duboribu.ecommerce.entity.QOrderItem.orderItem;
import static com.duboribu.ecommerce.entity.QPayment.payment;
import static com.duboribu.ecommerce.entity.member.QMember.member;

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

        List<FoReceiptOrderItem> list = jpaQueryFactory.select(new QFoReceiptOrderItem(item.id, book.title, orderItem.netPrice, orderItem.count))
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

    @Override
    public List<FoMyPageOrderResponse> findOrderListByUser(String userId) {

        List<FoMyPageOrderIdState> orderIds = jpaQueryFactory.select(new QFoMyPageOrderIdState(order.id, order.state))
                .from(order)
                .innerJoin(member)
                .on(order.member.eq(member))
                .where(order.member.id.eq(userId))
                .fetch();

        List<FoMyPageOrderItem> orderItemlist = jpaQueryFactory.select(new QFoMyPageOrderItem(order.id, orderItem.state,
                        delivery.wmsOrderState,
                        book.title,
                        book.price,
                        orderItem.count,
                        book.price.multiply(orderItem.count),
                        orderItem.dcPrice
                ))
                .from(order)
                .innerJoin(member)
                .on(order.member.eq(member))
                .innerJoin(orderItem)
                .on(orderItem.order.eq(order))
                .innerJoin(item)
                .on(orderItem.item.eq(item))
                .innerJoin(book)
                .on(item.id.eq(book.id))
                .leftJoin(delivery)
                .on(orderItem.delivery.eq(delivery))
                .where(member.id.eq(userId))
                .fetch();

        List<FoMyPageOrderResponse> list = new ArrayList<>();
        for (FoMyPageOrderIdState foMyPageOrderIdState : orderIds) {
            List<FoMyPageOrderItem> orderItems = orderItemlist.stream()
                    .filter(it -> it.getOrderId() == foMyPageOrderIdState.getOrderId())
                    .collect(Collectors.toList());

            int totalPrice = orderItems.stream().
                    mapToInt(it -> it.getUnitPrice())
                    .sum();

            list.add(new FoMyPageOrderResponse(foMyPageOrderIdState.getOrderId(),
                    foMyPageOrderIdState.getOrderState(),
                    totalPrice,
                    0,
                    orderItems
                    ));
        }

        return list;
    }
}
