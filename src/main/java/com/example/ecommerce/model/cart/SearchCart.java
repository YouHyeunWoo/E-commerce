package com.example.ecommerce.model.cart;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public class SearchCart {
    @Data
    @AllArgsConstructor
    public static class Product{
        private Long productId;
        private String productName;
        private Long amount;
        private Long unitPrice;
        private Long totalPrice;
    }

    @Data
    @AllArgsConstructor
    public static class ProductList{
        private Long totalPrice;
        private List<Product> productList;
    }
}
