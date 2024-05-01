package com.duboribu.ecommerce.warehouse.stock.service;

import com.duboribu.ecommerce.Utils.RestUtil;
import com.duboribu.ecommerce.admin.item.dto.CreateStockRequest;
import com.duboribu.ecommerce.entity.Item;
import com.duboribu.ecommerce.entity.Stock;
import com.duboribu.ecommerce.repository.ItemJpaRepository;
import com.duboribu.ecommerce.repository.StockJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockService {
    private final StockJpaRepository stockJpaRepository;
    private final ItemJpaRepository itemJpaRepository;

    @Transactional
    public boolean addStock(CreateStockRequest request) {
        if (request.getId() == null) {
            throw new IllegalArgumentException("상품 ID가 없습니다 잘못된 접근입니다.");
        }
        try {
            ResponseEntity<Boolean> response = RestUtil.get("http://localhost:8080/admin/item/exist/" + request.getId(), new HttpHeaders(), Boolean.class);
            Boolean exist = response.getBody();
            log.info("{} : ", exist.toString());
            if (exist.booleanValue()) {
                Item item = itemJpaRepository.findById(request.getId()).get();
                Optional<Stock> findStock = stockJpaRepository.findByItem(item);
                if (findStock.isPresent()) {
                    Stock stock = findStock.get();
                    stock.changeStock(request.getCount());
                    return true;
                }
                Stock stock = new Stock(item, request.getCount());
                stockJpaRepository.save(stock);
                return true;
            }
        } catch (Exception e) {
            log.error("{}", e.getMessage());
            return false;
        }
        return false;
    }
}
