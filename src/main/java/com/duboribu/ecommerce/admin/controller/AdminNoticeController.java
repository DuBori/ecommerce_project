package com.duboribu.ecommerce.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/admin/notice")
@RequiredArgsConstructor
public class AdminNoticeController {
    @GetMapping("")
    public String list() {
        return "admin/notice/list";
    }
    @GetMapping("/create")
    public String createPage() {
        return "admin/notice/create";
    }
    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id) {
        return "admin/notice/view";
    }
    @PostMapping("/create")
    public ResponseEntity create() {
        return null;
    }
    @PostMapping("/update/{id}")
    public ResponseEntity update(@PathVariable Long id) {
        return null;
    }
}
