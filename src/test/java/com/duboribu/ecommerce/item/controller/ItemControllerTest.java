package com.duboribu.ecommerce.item.controller;

import com.duboribu.ecommerce.entity.Book;
import com.duboribu.ecommerce.entity.Item;
import com.duboribu.ecommerce.entity.Price;
import com.duboribu.ecommerce.entity.Stock;
import com.duboribu.ecommerce.repository.BookJpaRepository;
import com.duboribu.ecommerce.repository.ItemJpaRepository;
import com.duboribu.ecommerce.repository.PriceJpaRepository;
import com.duboribu.ecommerce.repository.StockJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

@SpringBootTest
class ItemControllerTest {
    @Autowired
    StockJpaRepository stockJpaRepository;
    @Autowired
    BookJpaRepository bookJpaRepository;
    @Autowired
    ItemJpaRepository itemJpaRepository;
    @Autowired
    PriceJpaRepository priceJpaRepository;
    @Test
    @Rollback(value = false)
    @Transactional
    public void 상품을생성하고가격을형성한다() {
        Stock stock = new Stock(100);
        Stock savedStock = stockJpaRepository.save(stock);

        Book book = new Book("코딩북", "박정현", "테스트출판사", "001", savedStock);
        Book savedBook = bookJpaRepository.save(book);

        // Book에 대한 Price 생성 및 저장
        Price price = new Price(new BigDecimal(500), savedBook);
        priceJpaRepository.save(price);

        Optional<Item> optionalItem = itemJpaRepository.findById(savedBook.getId());


        if (optionalItem.isPresent()) {
            Item foundItem = optionalItem.get();
            foundItem.updatePrice(Collections.singletonList(price));
            Price savedPrice = foundItem.getPrices().get(0);
            Optional<Price> actualPrice = priceJpaRepository.findById(price.getId());
            Assertions.assertEquals(savedPrice, actualPrice.get());
        }
    }


}