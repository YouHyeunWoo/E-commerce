package com.example.ecommerce.controller;

import com.example.ecommerce.model.Cart;
import com.example.ecommerce.service.CartService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@AllArgsConstructor
@Slf4j
public class CartController {
    private final CartService cartService;

    @PostMapping("/putin")
    @PreAuthorize("hasRole(CLIENT)")
    public ResponseEntity<?> putIn(@RequestHeader(value = "Authorization") String totalToken,
                                   @RequestBody Cart.Request request) {
        this.cartService.putIn(totalToken, request);

        return null;
    }
}
