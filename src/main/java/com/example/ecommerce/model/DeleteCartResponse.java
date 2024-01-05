package com.example.ecommerce.model;

import com.example.ecommerce.domain.ProductEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteCartResponse {
    private Long productId;
    private String productName;

    public static DeleteCartResponse fromEntity(ProductEntity productEntity){
        return DeleteCartResponse.builder()
                .productId(productEntity.getProductId())
                .productName(productEntity.getProductName())
                .build();
    }
}
