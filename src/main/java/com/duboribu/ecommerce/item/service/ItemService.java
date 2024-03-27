package com.duboribu.ecommerce.item.service;

import com.duboribu.ecommerce.entity.Book;
import com.duboribu.ecommerce.entity.Item;
import com.duboribu.ecommerce.entity.Price;
import com.duboribu.ecommerce.entity.Stock;
import com.duboribu.ecommerce.item.dto.CreateBookRequest;
import com.duboribu.ecommerce.item.dto.CreateStockRequest;
import com.duboribu.ecommerce.item.dto.ResponseItem;
import com.duboribu.ecommerce.repository.ItemJpaRepository;
import com.duboribu.ecommerce.repository.PriceJpaRepository;
import com.duboribu.ecommerce.repository.StockJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemJpaRepository itemJpaRepository;
    private final PriceJpaRepository priceJpaRepository;
    private final StockJpaRepository stockJpaRepository;
    @Transactional
    public ResponseItem createItem(CreateBookRequest request) {
        Book book = new Book(request);
        Book savedBook = itemJpaRepository.save(book);

        Price price = new Price(new BigDecimal(request.getPrice()), savedBook);
        priceJpaRepository.save(price);
        return new ResponseItem(savedBook);
    }
    @Transactional
    public int addStock(CreateStockRequest request) {
        Optional<Item> findItem = itemJpaRepository.findById(request.getId());
        if (findItem.isPresent()) {
            Stock stock = new Stock(findItem.get(), request.getCount());
            stockJpaRepository.save(stock);
            return 1;
        }
        return 0;
    }
}
