package com.duboribu.ecommerce.front.notice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {
    @GetMapping("")
    public String notice() {
        return "front/notice";
    }
    @GetMapping("/notice/view/{id}")
    public String noticeView(@PathVariable int id) {
        return "front/notice-detials";
    }
}
