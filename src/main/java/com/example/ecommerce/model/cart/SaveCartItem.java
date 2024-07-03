package com.example.ecommerce.model.cart;

import com.example.ecommerce.domain.CartItemEntity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

public class SaveCartItem {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        @NotNull
        @Min(1)
        private Long quantity;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long cartItemId;
        private Long productId;
        private String productName;
        private Long quantity;
        private Long totalPrice;

        public static Response fromEntity(CartItemEntity cartItemEntity) {
            return Response.builder()
                    .cartItemId(cartItemEntity.getCartItemId())
                    .productId(cartItemEntity.getProductEntity().getProductId())
                    .productName(cartItemEntity.getProductEntity().getProductName())
                    .quantity(cartItemEntity.getQuantity())
                    .totalPrice(cartItemEntity.getTotalPrice())
                    .build();
        }
    }
}
