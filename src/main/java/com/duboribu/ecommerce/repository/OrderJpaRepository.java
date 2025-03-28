package com.duboribu.ecommerce.repository;

import com.duboribu.ecommerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
    int countAllByCreatedAtBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
