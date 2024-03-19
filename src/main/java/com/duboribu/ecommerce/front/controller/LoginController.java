package com.duboribu.ecommerce.front.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller(value = "LoginController")
@RequestMapping("/login")
public class LoginController {
    @RequestMapping()
    public String loginPage() {
        return "/login/login";
    }

    @RequestMapping("/signup")
    public String signPage() {
        return "/login/signup";
    }
}
