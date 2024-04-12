package com.duboribu.ecommerce.front.repository;

import com.duboribu.ecommerce.front.dto.response.FoItemResponse;
import com.duboribu.ecommerce.front.service.FoItemView;

import java.util.List;

public interface FoItemCustomRepository {
    List<FoItemResponse> normalList();

    FoItemView loadItemViewResponse(Long itemId);
}
