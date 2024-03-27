package com.duboribu.ecommerce.repository;

import com.duboribu.ecommerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
}
