package com.example.ecommerce.model.product;

import com.example.ecommerce.domain.ProductEntity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

public class ModifyProduct {

    @Data
    public static class Request {
        private String productName;
        private Long price;
        private Long stock;
        private String explanation;
    }

    @Data
    @Builder
    public static class Response {
        private Long productId;
        private String productName;
        private Long price;
        private Long stock;
        private String explanation;
        private LocalDateTime registerDate;
        private LocalDateTime modifiedDate;

        public static Response fromEntity(ProductEntity productEntity) {
            return Response.builder()
                    .productId(productEntity.getProductId())
                    .productName(productEntity.getProductName())
                    .price(productEntity.getPrice())
                    .stock(productEntity.getStock())
                    .explanation(productEntity.getExplanation())
                    .registerDate(productEntity.getRegisterDate())
                    .modifiedDate(productEntity.getModifiedDate())
                    .build();
        }
    }
}
