package com.example.ecommerce.model.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

public class SearchCartItem {
    @Getter
    @AllArgsConstructor
    public static class Product {
        private Long productId;
        private String productName;
        private Long quantity;
        private Long unitPrice;
        private Long totalPrice;
    }

    @Getter
    @AllArgsConstructor
    public static class ProductList {
        private Long totalPrice;
        private List<Product> productList;
    }
}
