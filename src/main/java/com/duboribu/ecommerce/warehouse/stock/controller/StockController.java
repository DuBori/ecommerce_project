package com.duboribu.ecommerce.warehouse.stock.controller;

import com.duboribu.ecommerce.admin.item.dto.CreateStockRequest;
import com.duboribu.ecommerce.warehouse.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/stock")
@Slf4j
public class StockController {

    private final StockService stockService;
    @GetMapping("/add")
    public ResponseEntity addStock(CreateStockRequest request) {
        if (!stockService.addStock(request)) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().build();
    }
}
