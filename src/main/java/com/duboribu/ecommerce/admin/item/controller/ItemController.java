package com.duboribu.ecommerce.admin.item.controller;

import com.duboribu.ecommerce.admin.item.dto.CreateBookRequest;
import com.duboribu.ecommerce.admin.item.dto.ResponseBook;
import com.duboribu.ecommerce.admin.item.dto.SearchItemRequest;
import com.duboribu.ecommerce.admin.item.repository.AdminItemCustomRepository;
import com.duboribu.ecommerce.admin.item.service.ItemService;
import com.duboribu.ecommerce.auth.domain.DefaultResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller(value = "adminItemController")
@RequiredArgsConstructor
@RequestMapping("/admin/item")
@Slf4j
public class ItemController {
    private final ItemService itemService;
    private final AdminItemCustomRepository adminItemCustomRepository;

    /**
     * 상품 조회
     * */
    @GetMapping("/list")
    public String list(SearchItemRequest searchItemRequest, Model model) {
        Page<ResponseBook> list = adminItemCustomRepository.list(searchItemRequest, PageRequest.of(searchItemRequest.getPage(), searchItemRequest.getPageSize()));
        model.addAttribute("list", list);
        return "/admin/item/list";
    }

    /**
     * 상품 상세
     **/
    @GetMapping("/view/{id}")
    public String detailPage(@PathVariable Long id, Model model) {
        ResponseBook byBookId = adminItemCustomRepository.findByBookId(id);
        model.addAttribute("item", byBookId);
        return "/admin/item/view";
    }

    /**
     * 상품 생성
     * */
    @GetMapping("/create")
    public String createItemPage() {
        return "/admin/item/create";
    }
    

    @PostMapping("/create")
    public ResponseEntity<DefaultResponse> createItem(@RequestBody CreateBookRequest request) {
        log.info("createBookReq : {}", request);
        ResponseBook responseItem = itemService.createItem(request);
        return new ResponseEntity<>(new DefaultResponse(responseItem), HttpStatus.OK);
    }

    /**
     * 상품 수정
     * */
    @PostMapping("/update")
    public String updateItem(CreateBookRequest request) {
        /*return "redirect:/admin/item/view" + item.getId();*/
        return null;
    }

    /**
     * 상품 유무 조회
     */
    @GetMapping("/exist/{id}")
    public ResponseEntity<Boolean> existItem(@PathVariable Long id) {
        return new ResponseEntity<>(itemService.isExist(id), HttpStatus.OK);
    }

}
