package com.example.ecommerce.exception.impl;

import com.example.ecommerce.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class ExistsSameProductInCart extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "장바구니에 같은 상품이 존재합니다.";
    }
}
