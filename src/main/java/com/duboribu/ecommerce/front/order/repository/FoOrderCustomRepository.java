package com.duboribu.ecommerce.front.order.repository;

import com.duboribu.ecommerce.front.order.dto.FoPaymentView;

public interface FoOrderCustomRepository {
    FoPaymentView findById(String id);
}
