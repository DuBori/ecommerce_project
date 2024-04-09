package com.duboribu.ecommerce.admin.item.service;

import com.duboribu.ecommerce.admin.item.dto.CreateBookRequest;
import com.duboribu.ecommerce.admin.item.dto.ResponseBook;
import com.duboribu.ecommerce.entity.Book;
import com.duboribu.ecommerce.entity.Price;
import com.duboribu.ecommerce.repository.ItemJpaRepository;
import com.duboribu.ecommerce.repository.PriceJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemJpaRepository itemJpaRepository;
    private final PriceJpaRepository priceJpaRepository;
    @Transactional
    public ResponseBook createItem(CreateBookRequest request) {
        Book book = new Book(request);
        Book savedBook = itemJpaRepository.save(book);

        Price price = new Price(new BigDecimal(request.getPrice()), savedBook);
        priceJpaRepository.save(price);
        return new ResponseBook(savedBook);
    }

    public boolean isExist(Long itemId) {
        log.info("{}", itemJpaRepository.existsById(itemId));
        return itemJpaRepository.existsById(itemId);
    }

    public ResponseBook findById(Long id) {
        return null;
    }
}
