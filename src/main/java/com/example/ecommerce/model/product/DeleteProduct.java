package com.example.ecommerce.model.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class DeleteProduct {
    @Getter
    @AllArgsConstructor
    public static class Request {
        private String productName;
    }

    @Getter
    @Builder
    public static class Response {
        private String productName;
        private String sellerName;
    }

}



