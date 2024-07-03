package com.example.ecommerce.model.order;

import lombok.Builder;

@Builder
public class OrderProduct {
    private Long productId;
    private String productName;
    private Long price;

}
