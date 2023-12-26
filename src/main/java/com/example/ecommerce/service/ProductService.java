package com.example.ecommerce.service;

import com.example.ecommerce.domain.MemberEntity;
import com.example.ecommerce.domain.ProductEntity;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.MemberRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.security.TokenProvider;
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
    public ProductEntity register(String totalToken, Product.Registration product) {
        String userName = getName(totalToken);

        MemberEntity memberEntity = this.memberRepository.findByName(userName)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 아이디 입니다."));

        return productRepository.save(product.toEntity(memberEntity));
    }

    //토큰의 앞 Bearer 부분을 제거한 후 토큰의 body 부분의 이름을 추출
    private String getName(String totalToken){
        String token = totalToken.substring(TOKEN_PREFIX.length());

        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token).getBody().getSubject();
    }


}
