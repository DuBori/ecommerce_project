package com.duboribu.ecommerce.front.item.controller;

import com.duboribu.ecommerce.admin.item.dto.SearchItemRequest;
import com.duboribu.ecommerce.front.category.service.FoCategoryService;
import com.duboribu.ecommerce.front.item.dto.FoItemView;
import com.duboribu.ecommerce.front.item.service.FoItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/item")
public class FoItemController {
    private final FoItemService foItemService;
    private final FoCategoryService foCategoryService;

    @GetMapping("/list")
    public String list(SearchItemRequest request, Model model) {
        model.addAttribute("dcList", foItemService.dcList(null, null));
        model.addAttribute("categoryItems", foItemService.normalList(request, null));
        model.addAttribute("newItems", foItemService.normalList(new SearchItemRequest(0, 6), null));
        model.addAttribute("categoryList", foCategoryService.list("book"));
        return "front/shop-grid";
    }
    @GetMapping("/{category}/list")
    public String categoryList(@PathVariable String category, SearchItemRequest request, Model model) {
        model.addAttribute("dcList", foItemService.dcList(null, null));
        model.addAttribute("categoryItems", foItemService.normalList(request, category));
        model.addAttribute("newItems", foItemService.normalList(new SearchItemRequest(0, 6), null));
        model.addAttribute("categoryList", foCategoryService.list("book"));
        return "front/shop-grid";
    }

    @GetMapping("/view/{id}")
    public String itemPage(@PathVariable Long id, Model model) {
        FoItemView attributeValue = foItemService.loadItemViewResponse(id);
        log.info("{}", attributeValue);
        model.addAttribute("item", attributeValue);
        return "front/shop-details";
    }

}
