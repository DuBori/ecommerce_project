package com.duboribu.ecommerce.warehouse.order.repository;

import com.duboribu.ecommerce.warehouse.entity.WmsOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WmsOrderItemJpaRepository extends JpaRepository<WmsOrderItem, Long> {
    Optional<WmsOrderItem> findByIdAndWmsOrder_CoCode(Long id, String coCode);
}
