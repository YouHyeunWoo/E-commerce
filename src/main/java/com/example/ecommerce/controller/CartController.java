package com.example.ecommerce.controller;

import com.example.ecommerce.model.Cart;
import com.example.ecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("/cart")
    @PreAuthorize("hasRole(CLIENT)")
    public Cart.Response putIn(@RequestParam Long productId,
                               @RequestHeader(value = "Authorization") String totalToken,
                               @RequestBody Cart.Request request) {
        return Cart.Response.fromEntity(this.cartService.putIn(productId, totalToken, request));
    }
}
