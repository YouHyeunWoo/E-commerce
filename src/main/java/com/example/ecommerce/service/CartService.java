package com.example.ecommerce.service;

import com.example.ecommerce.domain.CartEntity;
import com.example.ecommerce.domain.MemberEntity;
import com.example.ecommerce.exception.impl.AlreadyExistsCartException;
import com.example.ecommerce.exception.impl.NotExistsTokenException;
import com.example.ecommerce.model.cart.CreateCartResponse;
import com.example.ecommerce.repository.CartItemRepository;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.security.JwtToken;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final JwtToken jwtToken;

    //장바구니 생성
    public CreateCartResponse createCartForUser() {
        MemberEntity memberEntity = this.jwtToken.getMemberEntityFromAuthentication();
        boolean existsCart = this.cartRepository.existsByMemberEntity(memberEntity);

        if (existsCart) {
            throw new AlreadyExistsCartException();
        }

        return CreateCartResponse.fromEntity(this.cartRepository.save(CartEntity.builder()
                .memberEntity(memberEntity)
                .build()));
    }

    @Transactional
    public void removeAllCartItem() {
        MemberEntity memberEntity = this.jwtToken.getMemberEntityFromAuthentication();

        CartEntity cartEntity = this.cartRepository.findByMemberEntity(memberEntity)
                .orElseThrow(NotExistsTokenException::new);

        this.cartItemRepository.deleteAllByCartEntity(cartEntity);
    }
}
