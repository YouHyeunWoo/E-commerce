package com.example.ecommerce.model;

import com.example.ecommerce.domain.MemberEntity;
import com.example.ecommerce.domain.ProductEntity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

public class Product {

    @Data
    public static class Registration {
        private String productName;
        private Long price;
        private Long amount;
        private String explanation;

        public ProductEntity toEntity(MemberEntity memberEntity) {
            return ProductEntity.builder()
                    .memberEntity(memberEntity)
                    .productName(this.productName)
                    .price(this.price)
                    .amount(this.amount)
                    .explanation(this.explanation)
                    .registerDate(LocalDateTime.now())
                    .build();
        }
    }

    @Data
    @Builder
    public static class Response {
        private Long productId;
        private String productName;
        private Long price;
        private Long amount;
        private String explanation;
        private String sellerName;

        public static Response fromProductEntity(ProductEntity productEntity) {
            return Response.builder()
                    .productId(productEntity.getId())
                    .productName(productEntity.getProductName())
                    .price(productEntity.getPrice())
                    .amount(productEntity.getAmount())
                    .sellerName(productEntity.getMemberEntity().getName())
                    .explanation(productEntity.getExplanation())
                    .build();
        }
    }
}
