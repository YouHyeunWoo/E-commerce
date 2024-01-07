package com.example.ecommerce.model;

import com.example.ecommerce.domain.MemberEntity;
import com.example.ecommerce.domain.ProductEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

public class RegisterProduct {

    @Data
    public static class Registration {
        @NotBlank
        private String productName;
        @NotNull
        private Long price;
        @NotNull
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
                    .productId(productEntity.getProductId())
                    .productName(productEntity.getProductName())
                    .price(productEntity.getPrice())
                    .amount(productEntity.getAmount())
                    .sellerName(productEntity.getMemberEntity().getName())
                    .explanation(productEntity.getExplanation())
                    .build();
        }
    }
}
