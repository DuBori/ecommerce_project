package com.duboribu.ecommerce.order.controller;

import com.duboribu.ecommerce.auth.domain.DefaultResponse;
import com.duboribu.ecommerce.order.dto.OrderRequestDTO;
import com.duboribu.ecommerce.order.service.OmsOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
@RequestMapping("/orderApi")
@RequiredArgsConstructor
public class OmsOrderController {
    private final OmsOrderService omsOrderService;

    @PostMapping("/create")
    public ResponseEntity<DefaultResponse> createOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        log.info("orderRequestDTO : {}", orderRequestDTO);
        String merchant_uid = omsOrderService.create(orderRequestDTO);
        log.info("dd : {}: ", merchant_uid);
        return new ResponseEntity<>(new DefaultResponse(merchant_uid), HttpStatus.OK);
    }
}
