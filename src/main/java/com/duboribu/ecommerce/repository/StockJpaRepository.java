package com.duboribu.ecommerce.repository;

import com.duboribu.ecommerce.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockJpaRepository extends JpaRepository<Stock, Long> {
}
