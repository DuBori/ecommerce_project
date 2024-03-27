package com.duboribu.ecommerce.repository;

import com.duboribu.ecommerce.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemJpaRepository extends JpaRepository<Item, Long> {
}
