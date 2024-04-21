package com.duboribu.ecommerce.front.order.service;

import com.duboribu.ecommerce.front.order.dto.FoPaymentView;
import com.duboribu.ecommerce.front.order.repository.FoOrderCustomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor
public class FoOrderService {
    private final FoOrderCustomRepository foOrderCustomRepository;

    public FoPaymentView findById(String id) {
        return foOrderCustomRepository.findById(id);
    }
}
