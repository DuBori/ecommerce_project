package com.duboribu.ecommerce.admin.item.repository;

import com.duboribu.ecommerce.admin.item.dto.ResponseBook;
import com.duboribu.ecommerce.admin.item.dto.SearchItemRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminItemCustomRepository {

    ResponseBook findByBookId(Long id);

    Page<ResponseBook> list(SearchItemRequest searchItemRequest, Pageable pageable);
}
