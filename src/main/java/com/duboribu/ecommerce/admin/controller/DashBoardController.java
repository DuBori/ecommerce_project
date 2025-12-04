package com.duboribu.ecommerce.admin.controller;

import com.duboribu.ecommerce.Utils.RestUtil;
import com.duboribu.ecommerce.admin.member.service.BoMemberService;
import com.duboribu.ecommerce.admin.order.service.BoOrderService;
import com.duboribu.ecommerce.auth.domain.DefaultResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class DashBoardController {
    private final BoMemberService boMemberService;
    private final BoOrderService boOrderService;
    @GetMapping
    public String main(Model model) {
        model.addAttribute("memberCount", boMemberService.count());
        model.addAttribute("orderCount", boOrderService.count());
        model.addAttribute("revenueCount", boOrderService.revenueCount());
        return "admin/index";
    }

    @GetMapping("/login")
    public String adminLoginPage() {
        return "admin/login";
    }

    @GetMapping("/singOut")
    public String adminSingOut() {
        try {
            ResponseEntity<DefaultResponse> response = RestUtil.get("http://localhost:8080/auth/logout", null, DefaultResponse.class);
            DefaultResponse body = response.getBody();
            if (body.getResCode() == 200) {
                return "admin/login";
            }
        } catch (Exception e) {
            log.error("{}", e.getMessage());
        }
        throw new IllegalArgumentException("로그아웃 에러");
    }

    @GetMapping("/profile")
    public String adminProfile() {
        return "admin/users-profile";
    }
}
