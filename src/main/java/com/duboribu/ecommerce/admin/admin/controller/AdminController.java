package com.duboribu.ecommerce.admin.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/admin/admin")
@RequiredArgsConstructor
public class AdminController {

    @GetMapping("/wms/add")
    public String wmsStockAdd() {
        return "admin/wms/add";
    }

}
