package com.duboribu.ecommerce.repository;

import com.duboribu.ecommerce.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface OrderItemJpaRepository extends JpaRepository<OrderItem, Long> {
    @Query(value = "SELECT IFNULL(SUM(net_price), 0) FROM order_item", nativeQuery = true)
    Long totalPrice();

    @Query( value = "SELECT IFNULL(SUM(net_price), 0) FROM order_item WHERE created_at BETWEEN :startOfYear AND :endOfYear", nativeQuery = true)
    Integer totalPrice(@Param("startOfYear") LocalDateTime startOfYear, @Param("endOfYear") LocalDateTime endOfYear);
}
