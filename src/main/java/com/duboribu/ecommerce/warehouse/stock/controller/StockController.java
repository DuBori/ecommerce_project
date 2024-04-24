package com.duboribu.ecommerce.warehouse.stock.controller;

import com.duboribu.ecommerce.admin.item.dto.CreateStockRequest;
import com.duboribu.ecommerce.warehouse.stock.service.StockService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wms/stock")
@Slf4j
public class StockController {
    private final StockService stockService;
    @PostMapping("/add")
    public ResponseEntity addStock(HttpServletRequest request, @RequestBody CreateStockRequest createStockRequest) {
        log.info("wms request : {}", createStockRequest);

        if (!stockService.addStock(createStockRequest)) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().build();
    }

}
