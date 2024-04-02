package com.duboribu.ecommerce.warehouse.order.controller;

import com.duboribu.ecommerce.auth.domain.DefaultResponse;
import com.duboribu.ecommerce.warehouse.order.dto.request.CreateDeliveryRequest;
import com.duboribu.ecommerce.warehouse.order.dto.request.SelectDeliveryRequest;
import com.duboribu.ecommerce.warehouse.order.service.WmsOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController(value = "WmsOrderController")
@RequiredArgsConstructor
@RequestMapping("/wms/order")
public class OrderController {
    private final WmsOrderService wmsOrderService;

    /**
     * 발주조회
     */
    @GetMapping("/list")
    public ResponseEntity<DefaultResponse> list(SelectDeliveryRequest request) {
        return new ResponseEntity<>(new DefaultResponse<>(wmsOrderService.list(request)), HttpStatus.OK);
    }

    /**
     * 발주등록
     * */
    @PostMapping("/register")
    public ResponseEntity registerOrder(CreateDeliveryRequest request) {
        if (!wmsOrderService.register(request)) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().build();
    }

}
