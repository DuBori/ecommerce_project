package com.duboribu.ecommerce.front.item.controller;

import com.duboribu.ecommerce.front.item.service.FoItemService;
import com.duboribu.ecommerce.front.item.service.FoItemView;
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

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("normalList", foItemService.normalList());
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
