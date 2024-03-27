package com.duboribu.ecommerce.item.service;

import com.duboribu.ecommerce.entity.Book;
import com.duboribu.ecommerce.entity.Price;
import com.duboribu.ecommerce.item.dto.CreateBookRequest;
import com.duboribu.ecommerce.repository.BookJpaRepository;
import com.duboribu.ecommerce.repository.PriceJpaRepository;
import com.duboribu.ecommerce.repository.StockJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final BookJpaRepository bookJpaRepository;
    private final PriceJpaRepository priceJpaRepository;

    private StockJpaRepository stockJpaRepository;
    public void createItem(CreateBookRequest request) {
        Book book = new Book(request);
        Book savedBook = bookJpaRepository.save(book);

        Price price = new Price(new BigDecimal(request.getPrice()), savedBook);
        priceJpaRepository.save(price);
    }

}
