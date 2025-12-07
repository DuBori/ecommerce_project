package com.duboribu.ecommerce.front.item.service;

import com.duboribu.ecommerce.admin.item.dto.SearchItemRequest;
import com.duboribu.ecommerce.front.dto.response.FoItemResponse;
import com.duboribu.ecommerce.front.item.dto.FoItemView;
import com.duboribu.ecommerce.front.item.repository.FoItemCustomRepository;
import com.duboribu.ecommerce.front.order.dto.CreateOrderRequest;
import com.duboribu.ecommerce.front.order.dto.FoOrderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FoItemService {
    private final FoItemCustomRepository foItemCustomRepository;
    @Transactional
    public Page<FoItemResponse> normalList(SearchItemRequest request, String category) {
        request.matchedCategory(category);
        return foItemCustomRepository.normalList(request.getCategory(), PageRequest.of(request.getPage(), request.getPageSize()));
    }
    @Transactional
    public FoItemView loadItemViewResponse(Long itemId) {
        FoItemView foItemView = foItemCustomRepository.loadItemViewResponse(itemId);
        return foItemView;
    }
    @Transactional
    public FoOrderResponse itemViewResponses(CreateOrderRequest request) {
        return foItemCustomRepository.itemViewResponses(request);
    }

    public Page<FoItemResponse> dcList(SearchItemRequest request, String category) {
        return foItemCustomRepository.dcList(category, PageRequest.of(request.getPage(), request.getPageSize()));
    }

    public FoOrderResponse itemLikeViewResponses(String userId) {
        return foItemCustomRepository.itemLikeViewResponses(userId);
    }
}
