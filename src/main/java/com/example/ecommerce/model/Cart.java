package com.example.ecommerce.model;

import com.example.ecommerce.domain.CartEntity;
import com.example.ecommerce.domain.MemberEntity;
import com.example.ecommerce.domain.ProductEntity;
import lombok.Builder;
import lombok.Data;

public class Cart {
    @Data
    public static class Request{
        private String productName;
        private Long amount;

        public CartEntity toEntity(MemberEntity memberEntity, ProductEntity productEntity){
            return CartEntity.builder()
                    .unitPrice(productEntity.getPrice())
                    .amount(this.amount)
                    .totalPrice(productEntity.getPrice() * this.amount)
                    .productEntity(productEntity)
                    .memberEntity(memberEntity)
                    .build();
        }
    }

    @Data
    @Builder
    public static class Response{
        private Long productId;
        private String productName;
        private Long amount;
        private Long unitPrice;
        private Long totalPrice;

        public static Response fromEntity(CartEntity cartEntity){
            return Response.builder()
                    .productId(cartEntity.getCartId())
                    .productName(cartEntity.getProductEntity().getProductName())
                    .amount(cartEntity.getAmount())
                    .unitPrice(cartEntity.getUnitPrice())
                    .totalPrice(cartEntity.getTotalPrice())
                    .build();
        }
    }
}
