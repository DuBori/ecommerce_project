package com.duboribu.ecommerce.profile.controller;

import com.duboribu.ecommerce.Utils.eamil.EmailService;
import com.duboribu.ecommerce.Utils.eamil.dto.EmailMessage;
import com.duboribu.ecommerce.admin.item.dto.SearchItemRequest;
import com.duboribu.ecommerce.profile.dto.ContactRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
@RequiredArgsConstructor
@Slf4j
public class ProFileController {

    private final EmailService emailService;
    @Value("${protect.email}")
    private String adminEmail;

    @GetMapping()
    public String profile(SearchItemRequest request, Model model) {
        return "index";
    }


    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @PostMapping("/contact")
    public void sendMail(@RequestBody ContactRequest req) {
        emailService.sendMail(new EmailMessage(adminEmail, "[Contact] " + req.getName(), "From: " + req.getEmail() + "\n\n" + req.getMessage()));
    }
}
