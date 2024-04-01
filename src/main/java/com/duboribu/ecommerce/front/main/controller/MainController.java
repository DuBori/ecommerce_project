package com.duboribu.ecommerce.front.main.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/main")
@RequiredArgsConstructor
@Slf4j
public class MainController {
    @RequestMapping("")
    public String main(HttpServletRequest request, HttpServletResponse response) {
        return "index";
    }

}
