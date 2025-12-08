package com.duboribu.ecommerce.admin.exception;

public class AdminAccessDeniedException extends RuntimeException {
    private final int code;
    public AdminAccessDeniedException(String message) {
        super(message);
        this.code = 401;
    }
    public int getCode() {
        return code;
    }
}