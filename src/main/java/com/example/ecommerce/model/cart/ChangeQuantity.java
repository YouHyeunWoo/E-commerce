package com.example.ecommerce.model.cart;

import com.example.ecommerce.domain.CartItemEntity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ChangeQuantity {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        @NotNull
        @Min(1)
        private Long quantity;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Response {
        private Long productId;
        private String productName;
        private Long productQuantity;
        private Long totalPrice;

        public static Response fromEntity(CartItemEntity cartItemEntity) {
            return Response.builder()
                    .productId(cartItemEntity.getProductEntity().getProductId())
                    .productName(cartItemEntity.getProductEntity().getProductName())
                    .productQuantity(cartItemEntity.getQuantity())
                    .totalPrice(cartItemEntity.getTotalPrice())
                    .build();
        }
    }
}
