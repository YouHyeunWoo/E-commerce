package com.example.ecommerce.controller;

import com.example.ecommerce.model.cart.SearchProductResponse;
import com.example.ecommerce.model.product.DeleteProduct;
import com.example.ecommerce.model.product.GetProduct;
import com.example.ecommerce.model.product.ModifyProduct;
import com.example.ecommerce.model.product.RegisterProduct;
import com.example.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;

    //상품 등록
    @PostMapping("/product")
    @PreAuthorize("hasAuthority('SELLER')")
    public RegisterProduct.Response productRegistration(
            @Valid @RequestBody RegisterProduct.Registration request) {
        return this.productService.registerProduct(request);
    }

    //내가 등록한 상품 조회
    //판매자용 상품 검색 기능
    @GetMapping("/my-products")
    @PreAuthorize("hasAuthority('SELLER')")
    public List<GetProduct.Seller> productSearchForSeller() {
        return this.productService.searchProduct();
    }

    //고객용 상품 검색 기능 모든 사용자 이용 가능
    //상품 이름을 입력 >> 최근에 등록된 상품 순으로 검색
    @GetMapping("/products")
    public SearchProductResponse productSearchForClient(@RequestParam String productName,
                                                        Pageable pageable) {
        List<GetProduct.Client> productList =
                this.productService.searchByProductName(productName, pageable);

        return new SearchProductResponse(productName, productList);
    }

    //상품 수정
    @PutMapping("/product")
    @PreAuthorize("hasAuthority('SELLER')")
    public ModifyProduct.Response modifyProduct(
            @RequestBody ModifyProduct.Request request) {
        return ModifyProduct.Response.fromEntity(this.productService
                        .modifyProduct(request));
    }

    //상품 삭제
    @DeleteMapping("/product")
    @PreAuthorize("hasAuthority('SELLER')")
    public DeleteProduct.Response removeProduct(
            @RequestBody DeleteProduct.Request deleteRequest) {
        return this.productService.deleteProduct(deleteRequest);
    }
}
