package com.duboribu.ecommerce.front.item.repository;

import com.duboribu.ecommerce.front.dto.response.FoItemResponse;
import com.duboribu.ecommerce.front.item.service.FoItemView;

import java.util.List;

public interface FoItemCustomRepository {
    List<FoItemResponse> normalList();

    FoItemView loadItemViewResponse(Long itemId);
}
