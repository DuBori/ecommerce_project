package com.duboribu.ecommerce.common.handler;

import com.duboribu.ecommerce.auth.JwtException;
import com.duboribu.ecommerce.auth.domain.DefaultResponse;
import com.duboribu.ecommerce.order.OrderException;
import com.duboribu.ecommerce.warehouse.WmsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 인증, 인가 Handler
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<DefaultResponse> handleJwtException(JwtException e) {
        log.warn("JWT Error: {}", e.getMessage());
        return new ResponseEntity<>(new DefaultResponse(e.getMessage(), e.getCode()), HttpStatus.OK);
    }

    // 주문 Handler
    @ExceptionHandler(OrderException.class)
    public ResponseEntity<DefaultResponse> exceptionHandler(final OrderException e) {
        log.info("OrderException : {}", e.getMessage());
        return new ResponseEntity<>(new DefaultResponse(e.getMessage(), e.getCode()), HttpStatus.OK);
    }

    // wms Handler
    @ExceptionHandler(WmsException.class)
    public ResponseEntity<DefaultResponse> exceptionHandler(final WmsException e) {
        log.info("WmsException : {}", e.getMessage());
        return new ResponseEntity<>(new DefaultResponse(e.getMessage(), e.getCode()), HttpStatus.OK);
    }


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<DefaultResponse> exceptionHandler(final HttpRequestMethodNotSupportedException e) {
        log.info(e.getMessage());
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<DefaultResponse> exceptionHandler(final Exception e) {
        log.info("Exception : {}", e.getMessage());
        return new ResponseEntity<>(new DefaultResponse(DefaultResponse.SYSTEM_ERR_MSG, DefaultResponse.SYSTEM_ERR), HttpStatus.OK);
    }
}