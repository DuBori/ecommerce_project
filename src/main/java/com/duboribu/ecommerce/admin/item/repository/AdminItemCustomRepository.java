package com.duboribu.ecommerce.admin.item.repository;

import com.duboribu.ecommerce.admin.item.dto.ResponseBook;

public interface AdminItemCustomRepository {

    ResponseBook findByBookId(Long id);
}
