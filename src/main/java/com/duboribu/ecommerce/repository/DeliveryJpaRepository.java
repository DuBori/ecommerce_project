package com.duboribu.ecommerce.repository;

import com.duboribu.ecommerce.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryJpaRepository extends JpaRepository<Delivery, Long> {
}
