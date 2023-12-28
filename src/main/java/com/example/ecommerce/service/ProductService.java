package com.example.ecommerce.service;

import com.example.ecommerce.domain.MemberEntity;
import com.example.ecommerce.domain.ProductEntity;
import com.example.ecommerce.model.DeleteProduct;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.MemberRepository;
import com.example.ecommerce.repository.ProductRepository;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    private static final String TOKEN_PREFIX = "Bearer";
    @Value("{spring.jwt.secret}")
    private String secretKey;

    //상품 등록
    //판매자의 아이디가 존재하는지 확인 >> 존재하면 상품 등록 가능
    //같은 이름의 상품은 여러개 등록될 수 있다.
    public ProductEntity registerProduct(String totalToken, Product.Registration product) {
        String userName = this.getUserName(totalToken);

        MemberEntity memberEntity = getMemberEntity(userName);

        return productRepository.save(product.toEntity(memberEntity));
    }

    //상품 삭제
    //판매자 아이디와 상품 이름으로 상품이 존재하는지 확인
    public DeleteProduct.Response deleteProduct(String totalToken, DeleteProduct.Request removeProduct) {
        String userName = this.getUserName(totalToken);

        MemberEntity memberEntity = getMemberEntity(userName);

        boolean existsProduct = this.productRepository
                .existsByProductNameAndMemberEntity(
                        removeProduct.getProductName(), memberEntity);

        if (!existsProduct) {
            throw new RuntimeException("해당 판매자가 판매하는 상품이 아닙니다");
        }

        this.productRepository.deleteByProductNameAndMemberEntity(
                removeProduct.getProductName(), memberEntity);

        return DeleteProduct.Response.builder()
                .productName(removeProduct.getProductName())
                .sellerName(userName)
                .build();
    }

    private MemberEntity getMemberEntity(String userName) {
        return this.memberRepository.findByName(userName)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 아이디 입니다."));
    }

    //토큰의 앞 Bearer 부분을 제거한 후 토큰의 body 부분의 이름을 추출
    private String getUserName(String totalToken) {
        String token = totalToken.substring(TOKEN_PREFIX.length());

        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token).getBody().getSubject();
    }
}
