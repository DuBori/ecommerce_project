package com.duboribu.ecommerce.warehouse.order.repository;

import com.duboribu.ecommerce.warehouse.entity.WmsOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WmsOrderJpaRepository extends JpaRepository<WmsOrder, Long> {
    Optional<WmsOrder> findByCoCodeAndWmsOrderItem(String coCode, Long wmsOrderItem);
}
