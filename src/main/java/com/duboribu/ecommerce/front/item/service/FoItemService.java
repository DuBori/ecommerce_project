package com.duboribu.ecommerce.front.item.service;

import com.duboribu.ecommerce.admin.item.dto.SearchItemRequest;
import com.duboribu.ecommerce.front.dto.response.FoItemResponse;
import com.duboribu.ecommerce.front.item.repository.FoItemCustomRepository;
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
    public Page<FoItemResponse> normalList(SearchItemRequest request) {
        return foItemCustomRepository.normalList(PageRequest.of(request.getPage(), request.getPageSize()));
    }

    @Transactional
    public FoItemView loadItemViewResponse(Long itemId) {
        log.info("itemId : {}", itemId);
        FoItemView foItemView = foItemCustomRepository.loadItemViewResponse(itemId);
        log.info("foItemView : {}", foItemView);

        return foItemView;
    }

}
