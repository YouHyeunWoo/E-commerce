package com.example.ecommerce.controller;

import com.example.ecommerce.model.cart.ChangeQuantity;
import com.example.ecommerce.model.cart.RemoveProductInCartResponse;
import com.example.ecommerce.model.cart.SaveCartItem;
import com.example.ecommerce.model.cart.SearchCartItem;
import com.example.ecommerce.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CartItemController {
    private final CartItemService cartItemService;

    //장바구니에 아이템 담기
    @PostMapping("/item")
    @PreAuthorize("hasAnyAuthority('SELLER', 'CLIENT')")
    public SaveCartItem.Response putInCart(@RequestParam Long productId,
                                           @RequestBody SaveCartItem.Request request) {
        return this.cartItemService.putItemInCart(productId, request);
    }

    //장바구니 상품 조회
    @GetMapping("/itemView")
    @PreAuthorize("hasAnyAuthority('SELLER', 'CLIENT')")
    public SearchCartItem.ProductList searchCartItem() {
        List<SearchCartItem.Product> productList = this.cartItemService.searchCartItem();
        //상품들의 최총 금액을 모두 합하여 결제 해야 하는 총액을 저장
        Long totalPrice = productList.stream().mapToLong(SearchCartItem.Product::getTotalPrice).sum();

        return new SearchCartItem.ProductList(totalPrice, productList);
    }

    //장바구니 상품 수량 변경
    @PostMapping("/item/quantity/{productId}")
    @PreAuthorize("hasAnyAuthority('SELLER', 'CLIENT')")
    public ChangeQuantity.Response changeProductQuantity(@PathVariable Long productId,
                                                         @RequestBody ChangeQuantity.Request request) {
        return this.cartItemService.changeProductQuantity(productId, request.getQuantity());
    }

    //장바구니 상품 삭제
    @DeleteMapping("/item/{productId}")
    @PreAuthorize("hasAnyAuthority('SELLER', 'CLIENT')")
    public RemoveProductInCartResponse removeProductInCart(@PathVariable Long productId) {
        return this.cartItemService.removeProductInCart(productId);
    }

}
