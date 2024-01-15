package com.example.ecommerce.service;

import com.example.ecommerce.domain.CartEntity;
import com.example.ecommerce.domain.MemberEntity;
import com.example.ecommerce.domain.ProductEntity;
import com.example.ecommerce.model.SaveCart;
import com.example.ecommerce.model.SearchCart;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.MemberRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.security.JwtToken;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final JwtToken jwtToken;

    //장바구니에 상품 담기
    //재고가 부족하면 담기 실패
    public CartEntity putIn(Long productId, String totalToken, SaveCart.Request product) {
        String userName = jwtToken.getUserName(totalToken);

        ProductEntity productEntity = this.productRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 상품 입니다."));
        boolean exists = this.cartRepository.existsByProductEntity(productEntity);

        if (exists) {
            throw new RuntimeException("장바구니에 같은 상품이 담겨 있습니다.");
        }

        MemberEntity memberEntity = this.memberRepository.findByName(userName)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 계정 입니다."));

        if (product.getAmount() > productEntity.getAmount()) {
            throw new RuntimeException("재고가 부족합니다.");
        }

        return this.cartRepository.save(product.toEntity(memberEntity, productEntity));
    }

    //내 장바구니 검색
    public List<SearchCart.Product> searchCart(String totalToken) {
        String userName = jwtToken.getUserName(totalToken);
        MemberEntity memberEntity = this.memberRepository.findByName(userName)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 계정 입니다."));
        List<CartEntity> cartEntityList =
                this.cartRepository.findAllByMemberEntity(memberEntity);

        if (cartEntityList.isEmpty()) {
            throw new RuntimeException("장바구니에 담긴 상품이 없습니다.");
        }

        return cartEntityList.stream().map(e -> new SearchCart.Product(e.getProductEntity().getProductId(),
                        e.getProductEntity().getProductName(), e.getAmount(),
                        e.getUnitPrice(), e.getTotalPrice()))
                .collect(Collectors.toList());
    }

    //장바구니 상품 삭제
    public ProductEntity deleteCart(Long productId, String totalToken) {
        ProductEntity productEntity = this.productRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 상품 입니다."));

        String userName = jwtToken.getUserName(totalToken);

        MemberEntity memberEntity = this.memberRepository.findByName(userName)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 계정 입니다."));

        this.cartRepository.deleteByProductEntityAndMemberEntity(productEntity, memberEntity);

        return productEntity;
    }

}
