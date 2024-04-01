package com.duboribu.ecommerce.admin.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class DashBoardController {

    @RequestMapping
    public String main() {
        return "/admin/index";
    }

}
