package com.duboribu.ecommerce.order;

import com.duboribu.ecommerce.auth.domain.DefaultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice("com.duboribu.ecommerce.order")
public class OrderAdvisor {
    @ExceptionHandler(OrderException.class)
    public ResponseEntity<DefaultResponse> exceptionHandler(final OrderException e) {
        log.info(e.getMessage());
        return new ResponseEntity<>(new DefaultResponse(e.getMessage(), e.getCode()), HttpStatus.OK);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<DefaultResponse> exceptionHandler(final HttpRequestMethodNotSupportedException e) {
        log.info(e.getMessage());
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<DefaultResponse> exceptionHandler(final Exception e) {
        log.info(e.getMessage());
        return new ResponseEntity<>(new DefaultResponse(DefaultResponse.SYSTEM_ERR_MSG, DefaultResponse.SYSTEM_ERR), HttpStatus.OK);
    }
}
