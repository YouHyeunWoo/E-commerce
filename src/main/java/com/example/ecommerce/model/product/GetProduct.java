package com.example.ecommerce.model.product;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class GetProduct {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Seller {
        private Long productId;
        private Long price;
        private Long stock;
        private String explanation;
        private List<String> imageUrlList;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Client {
        private Long productId;
        private Long price;
        private Long stock;
        private String explanation;
        private String sellerName;
        private String sellerPhone;
        private LocalDateTime registerDate;
        private LocalDateTime modifiedDate;
        private List<String> imageUrlList;
    }

}
