package com.duboribu.ecommerce.repository;

import com.duboribu.ecommerce.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface PaymentJpaRepository extends JpaRepository<Payment, String> {
    int countAllByCreatedAtBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
