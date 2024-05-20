package com.example.ecommerce.model.product;

import lombok.Builder;
import lombok.Data;

public class DeleteProduct {
    @Data
    public static class Request {
        private String productName;
    }

    @Data
    @Builder
    public static class Response {
        private String productName;
        private String sellerName;
    }

}



