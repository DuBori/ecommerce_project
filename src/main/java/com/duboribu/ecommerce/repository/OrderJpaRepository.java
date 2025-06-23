package com.duboribu.ecommerce.repository;

import com.duboribu.ecommerce.entity.Order;
import com.duboribu.ecommerce.enums.OrderState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
    int countAllByCreatedAtBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<Order> findOrderByState(OrderState state);
}
