package com.duboribu.ecommerce.common.controller;

import com.duboribu.ecommerce.admin.exception.AdminAccessDeniedException;
import com.duboribu.ecommerce.front.exception.UserAccessLoginDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = "com.duboribu.ecommerce")  // 또는 특정 패키지
public class ViewExceptionHandler {

    @ExceptionHandler(AdminAccessDeniedException.class)
    public String handleAdminAccessDenied(AdminAccessDeniedException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "error/pages-error-401";  // View 반환
    }

    @ExceptionHandler(UserAccessLoginDeniedException.class)
    public String UserAccessLoginDeniedException(UserAccessLoginDeniedException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "login/login";
    }
}