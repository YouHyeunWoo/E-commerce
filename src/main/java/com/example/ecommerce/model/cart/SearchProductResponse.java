package com.example.ecommerce.model.cart;


import com.example.ecommerce.model.product.GetProduct;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SearchProductResponse {
    private String productName;
    private List<GetProduct.Client> productList;
}
