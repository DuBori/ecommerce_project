package com.duboribu.ecommerce.admin.item.controller;

import com.duboribu.ecommerce.admin.item.dto.CreateBookRequest;
import com.duboribu.ecommerce.admin.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController(value = "adminItemController")
@RequiredArgsConstructor
@RequestMapping("/admin/item")
@Slf4j
public class ItemController {

    private final ItemService itemService;

    /**
     * 상품 생성요청
     * */
    @PostMapping("/create")
    public void createItem(CreateBookRequest request) {
        itemService.createItem(request);
    }

    /**
     * 상품 유무 조회
     */
    @GetMapping("/exist/{id}")
    public ResponseEntity<Boolean> existItem(@PathVariable Long id) {
        return new ResponseEntity<>(itemService.isExist(id), HttpStatus.OK);
    }

}
