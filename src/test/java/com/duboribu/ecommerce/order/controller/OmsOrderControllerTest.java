package com.duboribu.ecommerce.order.controller;

import com.duboribu.ecommerce.order.dto.OrderRequestDTO;
import com.duboribu.ecommerce.order.service.OmsOrderService;
import com.duboribu.ecommerce.repository.MemberJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OmsOrderControllerTest {

    OrderRequestDTO orderRequestDTO;

    @InjectMocks
    OmsOrderController omsOrderController;

    @Mock
    OmsOrderService omsOrderService;

    @Mock
    MemberJpaRepository memberJpaRepository;


    @BeforeEach
    void setUp() {
        orderRequestDTO = new OrderRequestDTO();
    }

    @Test
    void createOrder() {

    }
}