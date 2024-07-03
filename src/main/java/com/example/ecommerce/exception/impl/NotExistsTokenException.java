package com.example.ecommerce.exception.impl;

import com.example.ecommerce.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NotExistsTokenException extends AbstractException {

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "존재하지 않는 토큰입니다.";
    }
}
