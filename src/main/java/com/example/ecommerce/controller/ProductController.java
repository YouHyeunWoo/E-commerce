package com.example.ecommerce.controller;

import com.example.ecommerce.domain.ProductEntity;
import com.example.ecommerce.model.DeleteProduct;
import com.example.ecommerce.model.GetProduct;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
                this.productService.registerProduct(totalToken, request);

        return Product.Response.fromProductEntity(productEntity);
    }

    //내가 등록한 상품 조회
    //판매자용 상품 검색 기능
    @GetMapping("/inquiry/seller")
    @PreAuthorize("hasRole(SELLER)")
    public List<GetProduct> getProduct(@RequestHeader(value = "Authorization") String totalToken){
        return this.productService.inquiryProduct(totalToken);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole(SELLER)")
    public DeleteProduct.Response productRemove(
            @RequestHeader(value = "Authorization") String totalToken,
            @RequestBody DeleteProduct.Request deleteRequest) {
        return this.productService.deleteProduct(totalToken, deleteRequest);
    }
}
