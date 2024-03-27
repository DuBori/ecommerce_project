package com.duboribu.ecommerce.repository;

import com.duboribu.ecommerce.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceJpaRepository extends JpaRepository<Price, Long> {
}
