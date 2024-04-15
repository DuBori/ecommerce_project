package com.duboribu.ecommerce.repository;

import com.duboribu.ecommerce.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {
}
