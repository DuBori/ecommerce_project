package com.duboribu.ecommerce.admin.order.controller;

import com.duboribu.ecommerce.admin.order.dto.SearchOrderRequest;
import com.duboribu.ecommerce.admin.order.repository.AdminOrderCustomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/order")
public class AdminOrderController {
    private final AdminOrderCustomRepository adminOrderCustomRepository;
    @GetMapping("/list")
    public String list(SearchOrderRequest request, Model model) {
        model.addAttribute("list", adminOrderCustomRepository.list(PageRequest.of(request.getPage(), request.getPageSize())));
        return "admin/order/list";
    }
}
