package com.duboribu.ecommerce.front.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/main")
@RequiredArgsConstructor
public class MainController {
    @RequestMapping("")
    public String main() {
        return "index";
    }
}
