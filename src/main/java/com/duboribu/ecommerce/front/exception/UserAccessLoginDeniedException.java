package com.duboribu.ecommerce.front.exception;

public class UserAccessLoginDeniedException extends RuntimeException{
    private final int code;
    public UserAccessLoginDeniedException(String message) {
        super(message);
        this.code = 401;
    }
    public int getCode() {
        return code;
    }
}
