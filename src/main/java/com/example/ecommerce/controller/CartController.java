package com.example.ecommerce.controller;

import com.example.ecommerce.model.SaveCart;
import com.example.ecommerce.model.SearchCart;
import com.example.ecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("/cart")
    @PreAuthorize("hasRole(CLIENT)")
    public SaveCart.Response putInCart(@RequestParam Long productId,
                                       @RequestHeader(value = "Authorization") String totalToken,
                                       @RequestBody SaveCart.Request request) {
        return SaveCart.Response.fromEntity(this.cartService.putIn(productId, totalToken, request));
    }

    @GetMapping("/cart")
    @PreAuthorize("hasRole(CLIENT)")
    public SearchCart.ProductList searchCart(@RequestHeader(value = "Authorization") String totalToken) {
        List<SearchCart.Product> productList = this.cartService.searchCart(totalToken);
        //상품들의 최총 금액을 모두 합하여 결제 해야 하는 총액을 저장
        Long totalPrice = productList.stream().mapToLong(SearchCart.Product::getTotalPrice).sum();

        return new SearchCart.ProductList(totalPrice, productList);
    }

    @DeleteMapping("/cart")
    @PreAuthorize("hasRole(CLIENT)")
    public ResponseEntity<?> deleteCart(@RequestParam Long productId,
                                        @RequestHeader(value = "Authorization") String totalToken) {
        this.cartService.deleteCart(productId, totalToken);

        return null;
    }
}
