package com.duboribu.ecommerce.front.qna.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/qna")
@RequiredArgsConstructor
public class FoQnaController {
    @GetMapping("")
    public String list() {
        return "front/qna/list";
    }

    @GetMapping("/write")
    public String write() {
        return "front/qna/write";
    }

    @GetMapping("/view")
    public String view() {
        return "front/qna/view";
    }
}
