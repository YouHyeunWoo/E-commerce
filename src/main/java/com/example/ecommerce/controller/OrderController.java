package com.example.ecommerce.controller;

import com.example.ecommerce.model.order.Order;
import com.example.ecommerce.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;

    //장바구니 상품 결제
    @PostMapping("/order")
    @PreAuthorize("hasAnyAuthority('SELLER', 'CLIENT')")
    public ResponseEntity<?> order(@RequestBody Order.Request orderRequest) {
        this.orderService.payOrder(orderRequest.getPayTotalPrice(), orderRequest.getAddress());

        return null;
    }

}
