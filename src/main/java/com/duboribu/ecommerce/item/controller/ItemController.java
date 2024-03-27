package com.duboribu.ecommerce.item.controller;

import com.duboribu.ecommerce.item.dto.CreateBookRequest;
import com.duboribu.ecommerce.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    /**
     * 상품 생성요청
     * */
    public void createItem(CreateBookRequest request) {
        itemService.createItem(request);
    }
}
