package com.example.ecommerce.model;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

public class GetProduct {

    @Data
    @AllArgsConstructor
    public static class Seller{
        private Long productId;
        private Long price;
        private Long amount;
        private String explanation;
    }

    @Data
    @AllArgsConstructor
    public static class Client{
        private Long productId;
        private Long price;
        private Long amount;
        private String explanation;
        private LocalDateTime registerDate;
        private LocalDateTime modifiedDate;
    }

}
