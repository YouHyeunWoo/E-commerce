package com.example.ecommerce.controller;

import com.example.ecommerce.domain.ProductEntity;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/product")
@Slf4j
public class ProductController {
    private final ProductService productService;

    //상품 등록
    @PostMapping("/registration")
    @PreAuthorize("hasRole(SELLER)")
    public Product.Response productRegistration(
            @RequestHeader(value = "Authorization") String totalToken,
            @RequestBody Product.Registration request) {
        ProductEntity productEntity =
                this.productService.register(totalToken, request);

        return Product.Response.fromProductEntity(productEntity);
    }
}
