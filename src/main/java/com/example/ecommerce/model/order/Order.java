package com.example.ecommerce.model.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class Order {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotNull
        @Min(1)
        private Long payTotalPrice;

        @NotBlank
        private String address;
    }

    @Getter
    @Builder
    public static class Response{
        private Long totalPrice;
        private List<OrderProduct> productInfo;
    }
}
