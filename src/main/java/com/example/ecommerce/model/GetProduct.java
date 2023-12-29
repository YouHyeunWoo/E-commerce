package com.example.ecommerce.model;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetProduct {
    private Long productId;
    private Long price;
    private Long amount;
    private String explanation;
}
