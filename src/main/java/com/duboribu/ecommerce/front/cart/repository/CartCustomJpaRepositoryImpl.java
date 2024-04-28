package com.duboribu.ecommerce.front.cart.repository;

import com.duboribu.ecommerce.entity.*;
import com.duboribu.ecommerce.front.cart.dto.CartRequest;
import com.duboribu.ecommerce.front.cart.dto.FoCartItemView;
import com.duboribu.ecommerce.front.cart.dto.QFoCartItemView;
import com.duboribu.ecommerce.front.order.dto.FoOrderItemView;
import com.duboribu.ecommerce.front.order.dto.FoOrderResponse;
import com.duboribu.ecommerce.front.order.dto.QFoOrderItemView;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.duboribu.ecommerce.entity.QBook.book;
import static com.duboribu.ecommerce.entity.QCart.cart;
import static com.duboribu.ecommerce.entity.QCartItem.cartItem;
import static com.duboribu.ecommerce.entity.QItem.item;
import static com.duboribu.ecommerce.entity.QStock.stock;
@Slf4j
@Repository
@RequiredArgsConstructor
public class CartCustomJpaRepositoryImpl implements CartCustomJpaRepository {
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public FoOrderResponse findCartByUserId(String userId) {
        List<FoCartItemView> foCartItemView = jpaQueryFactory.select(new QFoCartItemView(item.id, cartItem.id, cartItem.quantity))
                .from(cart)
                .leftJoin(cartItem)
                .on(cart.eq(cartItem.cart))
                .leftJoin(item)
                .on(cartItem.item.eq(item))
                .where(cart.order.isNull(), cart.member.id.eq(userId))
                .fetch();


        List<FoOrderItemView> list = jpaQueryFactory.select(new QFoOrderItemView(book.id, book.title, book.author, book.publisher,
                        book.filePath, book.price, stock.count, book.state, book.comment, book.information, book.weight))
                .from(cart)
                .innerJoin(cartItem)
                .on(cart.eq(cartItem.cart))
                .innerJoin(book)
                .on(cartItem.item.id.eq(book.id))
                .innerJoin(stock)
                .on(stock.item.id.eq(book.id))
                .where(cart.order.isNull(), cart.member.id.eq(userId))
                .fetch();

        if (list.isEmpty()) {
            return null;
        }

        int totalPrice = 0;
        for (FoOrderItemView itemview : list) {
            log.info("{}", "아이템 세팅");
            for (FoCartItemView cartItemView : foCartItemView) {
                if (itemview.getId().equals(cartItemView.getItemId())){
                    itemview.mactchedUntiyPrice(cartItemView.getQuantity());
                    itemview.mactchedCartItemId(cartItemView.getCartItemId());
                    break;
                }
            }
            totalPrice += itemview.getUnitPrice();
        }

        return new FoOrderResponse(getProductName(list), totalPrice, list);
    }

    @Override
    public boolean mergeIntoByCartRequest(String userId, List<CartRequest> list) {
        List<Tuple> results = jpaQueryFactory
                .select(cart, item.id, cartItem, item)
                .from(cart)
                .leftJoin(cartItem).on(cart.eq(cartItem.cart))
                .leftJoin(item).on(cartItem.item.eq(item))
                .where(cart.order.isNull(), cart.member.id.eq(userId))
                .fetch();

        Map<Long, CartItem> cartItemMap = new HashMap<>();
        List<Item> itemList = new ArrayList<>();

        for (Tuple result : results) {
            Long itemId = result.get(item.id);
            CartItem cartItem = result.get(QCartItem.cartItem);
            Item item = result.get(QItem.item);

            cartItemMap.put(itemId, cartItem);
            itemList.add(item);
        }
        Cart targetCart = results.get(0).get(cart);

        // 필요한 데이터를 사용하여 로직을 처리합니다.

        for (CartRequest cartRequest : list) {
            CartItem orDefault = cartItemMap.getOrDefault(cartRequest.getProductId(), null);
            if (orDefault != null) {
                orDefault.updateQuantity(cartRequest.getQuantity());
            } else {
                targetCart.getCartItemList().add(new CartItem(targetCart, findItemById(cartRequest.getProductId()), cartRequest.getQuantity()));
            }
        }

        return true;
    }

    private Item findItemById(Long productId) {
        return jpaQueryFactory.select(item)
                .from(item)
                .where(item.id.eq(productId))
                .fetchOne();
    }

    private String getProductName(List<FoOrderItemView> list) {
        if (list.isEmpty()) {
            return null;
        }
        if (list.size() > 1) {
            return list.get(0).getTitle() + "외 " + (list.size() - 1);
        }
        return list.get(0).getTitle();
    }
}
