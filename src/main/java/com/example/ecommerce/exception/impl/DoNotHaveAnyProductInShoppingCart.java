package com.example.ecommerce.exception.impl;

import com.example.ecommerce.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class DoNotHaveAnyProductInShoppingCart extends AbstractException {

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "장바구니에 상품이 없습니다.";
    }
}
