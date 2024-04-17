package com.duboribu.ecommerce.front.category.service;

import com.duboribu.ecommerce.front.category.dto.FoCategoryResponse;
import com.duboribu.ecommerce.front.category.repository.FoCategoryCustomJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FoCategoryService {
    private final FoCategoryCustomJpaRepository categoryCustomJpaRepository;

    public List<FoCategoryResponse> list(String book) {
        return categoryCustomJpaRepository.list(book);
    }
}
