package com.example.ecommerce.model.cart;

import com.example.ecommerce.domain.CartEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CreateCartResponse {
    private Long cartId;
    private Long memberId;

    public static CreateCartResponse fromEntity(CartEntity cartEntity){
        return CreateCartResponse.builder()
                .cartId(cartEntity.getCartId())
                .memberId(cartEntity.getMemberEntity().getId())
                .build();
    }
}
