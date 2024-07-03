package com.example.ecommerce.model.cart;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RemoveProductInCartResponse {
    private Long productId;
    private String productName;
}
