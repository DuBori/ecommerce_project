package com.duboribu.ecommerce.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/error")
public class ErrorController {

    @GetMapping("/{code}")
    public String errorPageCode(@PathVariable String code) {
        log.info("진입");
        switch (code) {
            case "404" :
                return "/error/pages-error-404";
            case "401" :
                return "/error/pages-error-401";
            default:
                return null;
        }
    }
}
