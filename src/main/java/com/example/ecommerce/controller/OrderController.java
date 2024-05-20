package com.example.ecommerce.controller;

import com.example.ecommerce.domain.CartEntity;
import com.example.ecommerce.model.order.Order;
import com.example.ecommerce.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;

    //장바구니 상품 결제
    @PostMapping("/order")
    @PreAuthorize("hasRole(CLIENT)")
    public ResponseEntity<?> payOrder(@RequestHeader(value = "Authorization") String totalToken,
                                      @RequestBody Order.Request payTotalPrice) {
        List<CartEntity> cartEntityList = this.orderService.payOrder(totalToken, payTotalPrice);

        return ResponseEntity.ok(cartEntityList);
    }

}
