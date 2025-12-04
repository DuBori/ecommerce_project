package com.duboribu.ecommerce.profile.controller;

import com.duboribu.ecommerce.admin.item.dto.SearchItemRequest;
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
public class ProFileController {
    @GetMapping()
    public String profile(SearchItemRequest request, Model model) {
        return "index";
    }
}
