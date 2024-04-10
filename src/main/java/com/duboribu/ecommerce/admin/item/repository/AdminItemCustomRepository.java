package com.duboribu.ecommerce.admin.item.repository;

import com.duboribu.ecommerce.admin.item.dto.ResponseBook;
import com.duboribu.ecommerce.admin.item.dto.SearchItemRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminItemCustomRepository {

    ResponseBook findByBookId(Long id);

    List<ResponseBook> list(SearchItemRequest searchItemRequest, Pageable pageable);
}
