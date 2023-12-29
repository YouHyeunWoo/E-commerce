package com.example.ecommerce.controller;

import com.example.ecommerce.domain.ProductEntity;
import com.example.ecommerce.model.DeleteProduct;
import com.example.ecommerce.model.GetProduct;
import com.example.ecommerce.model.ModifyProduct;
import com.example.ecommerce.model.RegisterProduct;
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
    public RegisterProduct.Response productRegistration(
            @RequestHeader(value = "Authorization") String totalToken,
            @RequestBody RegisterProduct.Registration request) {
        return RegisterProduct.Response
                .fromProductEntity(this.productService
                        .registerProduct(totalToken, request));
    }

    //내가 등록한 상품 조회
    //판매자용 상품 검색 기능
    @GetMapping("/search/seller")
    @PreAuthorize("hasRole(SELLER)")
    public List<GetProduct> searchProduct(
            @RequestHeader(value = "Authorization") String totalToken){
        return this.productService.searchProduct(totalToken);
    }

    //상품 수정
    @PutMapping("/modify")
    @PreAuthorize("hasRole(SELLER)")
    public ModifyProduct.Response modifyProduct(
            @RequestHeader(value = "Authorization") String totalToken,
            @RequestBody ModifyProduct.Request request){
        return ModifyProduct.Response
                .fromEntity(this.productService
                        .modifyProduct(totalToken, request));
    }

    //상품 삭제
    @DeleteMapping("/delete")
    @PreAuthorize("hasRole(SELLER)")
    public DeleteProduct.Response productRemove(
            @RequestHeader(value = "Authorization") String totalToken,
            @RequestBody DeleteProduct.Request deleteRequest) {
        return this.productService.deleteProduct(totalToken, deleteRequest);
    }
}
