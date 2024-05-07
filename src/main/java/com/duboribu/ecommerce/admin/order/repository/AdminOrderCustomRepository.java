package com.duboribu.ecommerce.admin.order.repository;

import com.duboribu.ecommerce.admin.order.dto.response.AdminOrderView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminOrderCustomRepository {
    Page<AdminOrderView> list(Pageable pageable);
}
