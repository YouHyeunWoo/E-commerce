package com.example.ecommerce.model.product;

import com.example.ecommerce.domain.MemberEntity;
import com.example.ecommerce.domain.ProductEntity;
import jakarta.validation.constraints.Min;
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
        @Min(10)
        private Long price;
        @NotNull
        @Min(1)
        private Long stock;
        private String explanation;

        public ProductEntity toEntity(MemberEntity memberEntity) {
            return ProductEntity.builder()
                    .memberEntity(memberEntity)
                    .productName(this.productName)
                    .price(this.price)
                    .stock(this.stock)
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
        private Long stock;
        private String explanation;
        private String sellerName;

        public static Response fromProductEntity(ProductEntity productEntity) {
            return Response.builder()
                    .productId(productEntity.getProductId())
                    .productName(productEntity.getProductName())
                    .price(productEntity.getPrice())
                    .stock(productEntity.getStock())
                    .sellerName(productEntity.getMemberEntity().getName())
                    .explanation(productEntity.getExplanation())
                    .build();
        }
    }
}
