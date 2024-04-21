package com.duboribu.ecommerce.front.item.repository;

import com.duboribu.ecommerce.front.dto.response.FoItemResponse;
import com.duboribu.ecommerce.front.item.dto.FoItemView;
import com.duboribu.ecommerce.front.order.dto.CreateOrderRequest;
import com.duboribu.ecommerce.front.order.dto.FoOrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FoItemCustomRepository {
    Page<FoItemResponse> normalList(Pageable pageable);

    FoItemView loadItemViewResponse(Long itemId);

    FoOrderResponse itemViewResponses(CreateOrderRequest request);
}
