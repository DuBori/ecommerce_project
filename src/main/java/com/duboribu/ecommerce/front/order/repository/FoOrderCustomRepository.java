package com.duboribu.ecommerce.front.order.repository;

import com.duboribu.ecommerce.front.main.domain.request.FoMyPageOrderResponse;
import com.duboribu.ecommerce.front.order.dto.FoPaymentView;

import java.util.List;

public interface FoOrderCustomRepository {
    FoPaymentView findById(String id);

    List<FoMyPageOrderResponse> findOrderListByUser(String userId);
}
