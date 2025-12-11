package com.duboribu.ecommerce.repository;

import com.duboribu.ecommerce.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByCode(String code);
    List<Category> findByCodeEquals(String code);
    Optional<Category> findByName(String name);
}
