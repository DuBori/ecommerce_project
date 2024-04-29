package com.duboribu.ecommerce.front.cart.repository;

import com.duboribu.ecommerce.front.cart.dto.request.CartRequest;
import com.duboribu.ecommerce.front.order.dto.FoOrderResponse;

import java.util.List;

public interface CartCustomJpaRepository {
    FoOrderResponse findCartByUserId(String userId);

    boolean mergeIntoByCartRequest(String userId, List<CartRequest> list);
}
