package com.example.ecommerce.service;

import com.example.ecommerce.domain.CartEntity;
import com.example.ecommerce.domain.CartItemEntity;
import com.example.ecommerce.domain.MemberEntity;
import com.example.ecommerce.domain.ProductEntity;
import com.example.ecommerce.exception.impl.*;
import com.example.ecommerce.model.cart.ChangeQuantity;
import com.example.ecommerce.model.cart.RemoveProductInCartResponse;
import com.example.ecommerce.model.cart.SaveCartItem;
import com.example.ecommerce.model.cart.SearchCartItem;
import com.example.ecommerce.repository.CartItemRepository;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.security.JwtToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartItemService {
    private final JwtToken jwtToken;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;

    //장바구니에 상품 담기
    //재고가 부족하면 담기 실패
    @Transactional
    public SaveCartItem.Response putItemInCart(Long productId, SaveCartItem.Request product) {
        MemberEntity memberEntity = this.jwtToken.getMemberEntityFromAuthentication();

        ProductEntity productEntity = this.productRepository.findByProductId(productId)
                .orElseThrow(NotExistsProductException::new);
        Long unitPrice = productEntity.getPrice();
        CartEntity cartEntity = this.cartRepository.findByMemberEntity(memberEntity)
                .orElseThrow(NotExistsAccountException::new);

        boolean exists = this.cartItemRepository
                .existsByProductEntityAndCartEntity(productEntity, cartEntity);

        if (exists) {
            throw new ExistsSameProductInCartException();
        }

        if (product.getQuantity() > productEntity.getStock()) {
            throw new OutOfStockException();
        }

        return SaveCartItem.Response.fromEntity(
                this.cartItemRepository.save(CartItemEntity.builder()
                        .cartEntity(cartEntity)
                        .productEntity(productEntity)
                        .quantity(product.getQuantity())
                        .totalPrice(unitPrice * product.getQuantity())
                        .build()));
    }

    //내 장바구니 상품 검색
    public List<SearchCartItem.Product> searchCartItem() {
        MemberEntity memberEntity = this.jwtToken.getMemberEntityFromAuthentication();
        CartEntity cartEntity = this.cartRepository.findByMemberEntity(memberEntity)
                .orElseThrow(NotExistsAccountException::new);
        List<CartItemEntity> cartitemEntityList =
                this.cartItemRepository.findAllByCartEntity(cartEntity);

        if (cartitemEntityList.isEmpty()) {
            throw new DoNotHaveAnyProductInShoppingCartException();
        }

        return cartitemEntityList.stream().map(product -> new SearchCartItem.Product(product.getProductEntity().getProductId(),
                        product.getProductEntity().getProductName(), product.getQuantity(),
                        product.getProductEntity().getPrice(), product.getTotalPrice()))
                .collect(Collectors.toList());
    }

    //장바구니 상품 수량 변경
    @Transactional
    public ChangeQuantity.Response changeProductQuantity(Long productId, Long quantity) {
        MemberEntity memberEntity = this.jwtToken.getMemberEntityFromAuthentication();
        CartEntity cartEntity = this.cartRepository.findByMemberEntity(memberEntity)
                .orElseThrow(NotExistsAccountException::new);
        ProductEntity productEntity = this.productRepository.findByProductId(productId)
                .orElseThrow(NotExistsProductException::new);

        //재고 부족 예외처리
        if (productEntity.getStock() < quantity) {
            throw new OutOfStockException();
        }

        CartItemEntity cartItemEntity = this.cartItemRepository
                .findByCartEntityAndProductEntity(cartEntity, productEntity)
                .orElseThrow(NotExistsProductInCartException::new);

        //더티체킹 >> JPA의 핵심 기능으로 엔티티의 상태 변화를 효율적으로 추적하고, 트랜잭션이 커밋될 때 변경사항을
        //데이터베이스에 자동으로 반영하는 장점
        cartItemEntity.changeQuantity(quantity);

        return ChangeQuantity.Response.fromEntity(cartItemEntity);
    }

    //장바구니에서 상품 삭제
    @Transactional
    public RemoveProductInCartResponse removeProductInCart(Long productId) {
        MemberEntity memberEntity = this.jwtToken.getMemberEntityFromAuthentication();
        CartEntity cartEntity = this.cartRepository.findByMemberEntity(memberEntity)
                .orElseThrow(NotExistsAccountException::new);
        ProductEntity productEntity = this.productRepository.findByProductId(productId)
                .orElseThrow(NotExistsProductException::new);

        boolean exists = this.cartItemRepository.existsByProductEntityAndCartEntity(productEntity, cartEntity);

        if(!exists){
            throw new NotExistsProductInCartException();
        }

        this.cartItemRepository.deleteByCartEntityAndProductEntity(cartEntity, productEntity);

        return RemoveProductInCartResponse.builder()
                .productId(productId)
                .productName(productEntity.getProductName())
                .build();
    }
}
