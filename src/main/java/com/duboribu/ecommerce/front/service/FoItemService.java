package com.duboribu.ecommerce.front.service;

import com.duboribu.ecommerce.front.dto.response.FoItemResponse;
import com.duboribu.ecommerce.front.repository.FoItemCustomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FoItemService {
    private final FoItemCustomRepository foItemCustomRepository;
    @Transactional
    public List<FoItemResponse> normalList() {
        return foItemCustomRepository.normalList();
    }

    @Transactional
    public FoItemView loadItemViewResponse(Long itemId) {
        log.info("itemId : {}", itemId);
        FoItemView foItemView = foItemCustomRepository.loadItemViewResponse(itemId);
        log.info("foItemView : {}", foItemView);

        return foItemView;
    }

}
