package com.duboribu.ecommerce.warehouse.order.repository;

import com.duboribu.ecommerce.warehouse.entity.WmsOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WmsOrderJpaRepository extends JpaRepository<WmsOrder, Long> {
}
