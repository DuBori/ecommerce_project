package com.duboribu.ecommerce.common;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {
    @RequestMapping(value = "/error", produces = "text/html")
    public String handleError(HttpServletRequest request) {
        // 오류 코드 가져오기
        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        // 401 Unauthorized 오류 처리
        if (statusCode != null && statusCode == HttpStatus.UNAUTHORIZED.value()) {
            return "error/pages-error-401";
        }

        // 404 Not Found 오류 처리
        if (statusCode != null && statusCode == HttpStatus.NOT_FOUND.value()) {
            return "error/pages-error-404";
        }

        // 기타 오류는 기본 오류 페이지로 처리
        return "error/pages-error-500";
    }

}