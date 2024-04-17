package com.duboribu.ecommerce.admin.category.service;

import com.duboribu.ecommerce.admin.category.dto.CategoryResponse;
import com.duboribu.ecommerce.admin.category.dto.CreateCategoryRequest;
import com.duboribu.ecommerce.admin.category.dto.SearchCategory;
import com.duboribu.ecommerce.admin.category.repository.CategoryCustomJpaRepository;
import com.duboribu.ecommerce.entity.Category;
import com.duboribu.ecommerce.repository.CategoryJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminCategoryService {
    private final CategoryCustomJpaRepository categoryCustomJpaRepository;
    private final CategoryJpaRepository categoryJpaRepository;

    @Transactional
    public Page<CategoryResponse> list(SearchCategory searchCategory) {
        return categoryCustomJpaRepository.list(PageRequest.of(searchCategory.getPage(), searchCategory.getSize()));
    }

    @Transactional
    public CategoryResponse create(CreateCategoryRequest request) {
        Category newCategory = new Category(request);
        Category save = categoryJpaRepository.save(newCategory);
        if (request.getParentId() != null) {
            Optional<Category> findCategory = categoryJpaRepository.findById(request.getParentId());
            if (findCategory.isPresent()) {
                Category category = findCategory.get();
                category.addNewCategory(save);
                return new CategoryResponse(save);
            }
            throw new IllegalArgumentException("부모 코드가 존재하지 않습니다");
        }
        return new CategoryResponse(save);
    }

    public CategoryResponse findById(Long id) {
        return categoryCustomJpaRepository.findById(id).get();
    }

    public void delete(Long id) {
        categoryJpaRepository.deleteById(id);
    }
}
