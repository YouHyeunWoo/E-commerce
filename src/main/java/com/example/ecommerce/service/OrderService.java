package com.example.ecommerce.service;

import com.example.ecommerce.domain.CartEntity;
import com.example.ecommerce.domain.MemberEntity;
import com.example.ecommerce.domain.OrderEntity;
import com.example.ecommerce.exception.impl.DoNotHaveAnyProductInShoppingCart;
import com.example.ecommerce.exception.impl.DoNotHaveEnoughBalance;
import com.example.ecommerce.exception.impl.NotExistsAccount;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.MemberRepository;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.security.JwtToken;
import com.example.ecommerce.type.PaymentStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Getter
@RequiredArgsConstructor
@Builder
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final JwtToken jwtToken;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;

    public List<CartEntity> payOrder(String totalToken, Order.Request payTotalPrice) {
        String userName = this.jwtToken.getUserName(totalToken);
        MemberEntity memberEntity = this.memberRepository.findByName(userName)
                .orElseThrow(NotExistsAccount::new);

        List<CartEntity> cartEntityList =
                this.cartRepository.findAllByMemberEntity(memberEntity);

        if (cartEntityList.isEmpty()) {
            throw new DoNotHaveAnyProductInShoppingCart();
        }

        Long totalPrice = cartEntityList.stream().mapToLong(CartEntity::getTotalPrice).sum();

        if (payTotalPrice.getPayTotalPrice() < totalPrice) {
            throw new DoNotHaveEnoughBalance();
        }

        this.orderRepository.saveAll(cartEntityList.stream().map(e -> OrderEntity.builder()
                        .cartEntity(e)
                        .paymentStatus(PaymentStatus.PAYMENT)
                        .build())
                .toList());

        return cartEntityList;
    }
}
