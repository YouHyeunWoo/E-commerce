package com.example.ecommerce.controller;

import com.example.ecommerce.model.cart.CreateCartResponse;
import com.example.ecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    //장바구니 생성
    @PostMapping("/cart")
    @PreAuthorize("hasAnyAuthority('SELLER', 'CLIENT')")
    public CreateCartResponse createCart() {
        return this.cartService.createCartForUser();
    }

    //장바구니 비우기
    @DeleteMapping("/cart/all-item")
    @PreAuthorize("hasAnyAuthority('SELLER', 'CLIENT')")
    public void removeAllCartItem() {
        this.cartService.removeAllCartItem();
    }
}
