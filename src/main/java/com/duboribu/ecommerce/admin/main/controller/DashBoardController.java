package com.duboribu.ecommerce.admin.main.controller;

import com.duboribu.ecommerce.Utils.RestUtil;
import com.duboribu.ecommerce.auth.domain.DefaultResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class DashBoardController {
    @RequestMapping
    public String main() {
        return "/admin/index";
    }

    @RequestMapping("/login")
    public String adminLoginPage() {
        return "/admin/login";
    }
    @RequestMapping("/singOut")
    public String adminSingOut() {
        try {
            ResponseEntity<DefaultResponse> response = RestUtil.get("http://localhost:8080/auth/logout", null, DefaultResponse.class);
            DefaultResponse body = response.getBody();
            if (body.getResCode() == 200) {
                return "/admin/login";
            }
        } catch (Exception e) {
            log.error("{}", e.getMessage());
        }
        throw new IllegalArgumentException("로그아웃 에러");
    }
}
