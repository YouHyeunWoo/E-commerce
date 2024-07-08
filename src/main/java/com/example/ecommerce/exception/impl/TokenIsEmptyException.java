package com.example.ecommerce.exception.impl;

import com.example.ecommerce.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class TokenIsEmptyException extends AbstractException {

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "토큰이 비어있습니다";
    }
}
