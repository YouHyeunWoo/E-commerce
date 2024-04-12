package com.example.ecommerce.service;

import com.example.ecommerce.domain.CartEntity;
import com.example.ecommerce.domain.MemberEntity;
import com.example.ecommerce.domain.ProductEntity;
import com.example.ecommerce.exception.impl.*;
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
                .orElseThrow(NotExistsProduct::new);

        boolean exists = this.cartRepository.existsByProductEntity(productEntity);

        if (exists) {
            throw new ExistsSameProductInCart();
        }

        MemberEntity memberEntity = this.memberRepository.findByName(userName)
                .orElseThrow(NotExistsAccount::new);

        if (product.getAmount() > productEntity.getAmount()) {
            throw new OutOfStock();
        }

        return this.cartRepository.save(product.toEntity(memberEntity, productEntity));
    }

    //내 장바구니 검색
    public List<SearchCart.Product> searchCart(String totalToken) {
        String userName = jwtToken.getUserName(totalToken);
        MemberEntity memberEntity = this.memberRepository.findByName(userName)
                .orElseThrow(NotExistsAccount::new);
        List<CartEntity> cartEntityList =
                this.cartRepository.findAllByMemberEntity(memberEntity);

        if (cartEntityList.isEmpty()) {
            throw new DoNotHaveAnyProductInShoppingCart();
        }

        return cartEntityList.stream().map(product -> new SearchCart.Product(product.getProductEntity().getProductId(),
                        product.getProductEntity().getProductName(), product.getAmount(),
                        product.getUnitPrice(), product.getTotalPrice()))
                .collect(Collectors.toList());
    }

    //장바구니 상품 삭제
    public ProductEntity deleteCart(Long productId, String totalToken) {
        ProductEntity productEntity = this.productRepository.findByProductId(productId)
                .orElseThrow(NotExistsProduct::new);

        String userName = jwtToken.getUserName(totalToken);

        MemberEntity memberEntity = this.memberRepository.findByName(userName)
                .orElseThrow(NotExistsAccount::new);

        this.cartRepository.deleteByProductEntityAndMemberEntity(productEntity, memberEntity);

        return productEntity;
    }

}
