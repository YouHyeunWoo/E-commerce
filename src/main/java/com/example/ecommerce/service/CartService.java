package com.example.ecommerce.service;

import com.example.ecommerce.domain.CartEntity;
import com.example.ecommerce.domain.MemberEntity;
import com.example.ecommerce.domain.ProductEntity;
import com.example.ecommerce.model.Cart;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.MemberRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.security.JwtToken;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final JwtToken jwtToken;

    public CartEntity putIn(Long productId, String totalToken, Cart.Request product) {
        String userName = jwtToken.getUserName(totalToken);

        ProductEntity productEntity = this.productRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 상품 입니다."));
        MemberEntity memberEntity = this.memberRepository.findByName(userName)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 계정 입니다."));

        if (product.getAmount() > productEntity.getAmount()) {
            throw new RuntimeException("재고가 부족합니다.");
        }

        return this.cartRepository.save(product.toEntity(memberEntity, productEntity));
    }
}
