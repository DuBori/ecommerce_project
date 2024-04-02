package com.duboribu.ecommerce.warehouse.order.controller;

import com.duboribu.ecommerce.warehouse.order.dto.WmsOrderInfo;
import com.duboribu.ecommerce.warehouse.order.dto.request.ProcessDeliveryRequest;
import com.duboribu.ecommerce.warehouse.order.service.WmsOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/wms/order")
public class ProcessController {
    private final WmsOrderService wmsOrderService;
    /**
     * 프로세스 진행
     * */
    @PostMapping("/process")
    public List<WmsOrderInfo> processList(ProcessDeliveryRequest request) {
        return wmsOrderService.processList(request);
    }
}
