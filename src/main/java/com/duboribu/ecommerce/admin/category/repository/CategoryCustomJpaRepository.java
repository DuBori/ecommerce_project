package com.duboribu.ecommerce.admin.category.repository;

import com.duboribu.ecommerce.admin.category.dto.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CategoryCustomJpaRepository {
    Page<CategoryResponse> list(Pageable pageable);

    Optional<CategoryResponse> findById(Long id);
}
