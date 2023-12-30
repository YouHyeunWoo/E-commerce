package com.example.ecommerce.model;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SearchProductResponse {
    private String productName;
    private List<GetProduct.Client> productList;
}
