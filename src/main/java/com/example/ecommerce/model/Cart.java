package com.example.ecommerce.model;

import lombok.Data;

public class Cart {
    @Data
    public static class Request{
        private Long productId;
        private String productName;
        private Long amount;
    }
}
