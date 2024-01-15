package com.example.ecommerce.exception.impl;

import com.example.ecommerce.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class AlreadyExistsAccount extends AbstractException {

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "이미 존재하는 계정입니다.";
    }
}
