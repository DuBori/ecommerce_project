package com.duboribu.ecommerce.front.main.controller;

import com.duboribu.ecommerce.front.category.service.FoCategoryService;
import com.duboribu.ecommerce.front.item.service.FoItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
@RequiredArgsConstructor
@Slf4j
public class MainController {
    private final FoItemService foItemService;
    private final FoCategoryService foCategoryService;
    @GetMapping
    public String main(Model model) {
        model.addAttribute("normalList", foItemService.normalList());
        model.addAttribute("categoryList", foCategoryService.list("book"));
        return "front/index";
    }



}
