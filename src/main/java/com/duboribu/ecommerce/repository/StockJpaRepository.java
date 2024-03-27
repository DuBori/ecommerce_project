package com.duboribu.ecommerce.repository;

import com.duboribu.ecommerce.entity.Book;
import com.duboribu.ecommerce.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockJpaRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByItem(Book book);
}
