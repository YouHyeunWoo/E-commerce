package com.example.ecommerce.model.product;

import com.example.ecommerce.domain.ProductEntity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RegisterProduct {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
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
    }

    @Getter
    @Builder
    public static class Response {
        private Long productId;
        private String productName;
        private Long price;
        private Long stock;
        private String explanation;
        private String sellerName;
        private String imageUrl;

        public static Response from(ProductEntity productEntity, String imageUrl) {
            return Response.builder()
                    .productId(productEntity.getProductId())
                    .productName(productEntity.getProductName())
                    .price(productEntity.getPrice())
                    .stock(productEntity.getStock())
                    .sellerName(productEntity.getMemberEntity().getName())
                    .explanation(productEntity.getExplanation())
                    .imageUrl(imageUrl)
                    .build();
        }
    }
}
