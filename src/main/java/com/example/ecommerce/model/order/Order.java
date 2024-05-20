package com.example.ecommerce.model.order;

import lombok.Data;

public class Order {

    @Data
    public static class Request{
        private Long payTotalPrice;
    }
}
