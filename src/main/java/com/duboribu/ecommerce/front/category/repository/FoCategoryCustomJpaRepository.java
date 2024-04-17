package com.duboribu.ecommerce.front.category.repository;

import com.duboribu.ecommerce.front.category.dto.FoCategoryResponse;

import java.util.List;

public interface FoCategoryCustomJpaRepository {
    List<FoCategoryResponse> list(String book);
}
