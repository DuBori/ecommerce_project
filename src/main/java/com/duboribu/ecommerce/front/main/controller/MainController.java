package com.duboribu.ecommerce.front.main.controller;

import com.duboribu.ecommerce.front.service.FoItemService;
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
    @GetMapping
    public String main(Model model) {
        model.addAttribute("normalList", foItemService.normalList());
        return "front/index";
    }



}
