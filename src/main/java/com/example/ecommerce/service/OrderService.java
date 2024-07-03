package com.example.ecommerce.service;

import com.example.ecommerce.domain.*;
import com.example.ecommerce.exception.impl.DoNotHaveEnoughBalanceException;
import com.example.ecommerce.exception.impl.NotExistsAccountException;
import com.example.ecommerce.exception.impl.NotExistsProductInCartException;
import com.example.ecommerce.repository.*;
import com.example.ecommerce.security.JwtToken;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.ecommerce.type.PaymentStatus.PAYMENT;

@Service
@Getter
@RequiredArgsConstructor
@Builder
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final JwtToken jwtToken;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void payOrder(Long payTotalPrice, String address) {
        MemberEntity memberEntity = this.jwtToken.getMemberEntityFromAuthentication();
        CartEntity cartEntity = this.cartRepository.findByMemberEntity(memberEntity)
                .orElseThrow(NotExistsAccountException::new);
        List<CartItemEntity> cartItemEntityList =
                this.cartItemRepository.findAllByCartEntity(cartEntity);

        //장바구니에 상품이 없으면 주문 불가
        if (cartItemEntityList.isEmpty()) {
            throw new NotExistsProductInCartException();
        }

        Long itemTotalPrice = cartItemEntityList.stream().mapToLong(CartItemEntity::getTotalPrice).sum();

        //결제 금액이 상품 총 금액과 같지 않으면 결제 취소
        if (!Objects.equals(payTotalPrice, itemTotalPrice)) {
            throw new DoNotHaveEnoughBalanceException();
        }

        //주문 생성
        OrderEntity orderEntity = this.orderRepository.save(OrderEntity.builder()
                .cartEntity(cartEntity)
                .memberEntity(memberEntity)
                .totalPrice(itemTotalPrice)
                .orderAddress(address)
                .orderDate(LocalDateTime.now())
                .paymentStatus(PAYMENT)
                .build());

        //장바구니 상품을 주문 상품으로 옮김
        List<OrderItemEntity> orderItemEntityList = cartItemEntityList.stream()
                .map(cartItem -> OrderItemEntity.builder()
                        .orderEntity(orderEntity)
                        .productEntity(cartItem.getProductEntity())
                        .quantity(cartItem.getQuantity())
                        .totalPrice(cartItem.getTotalPrice())
                        .build()).toList();
        this.orderItemRepository.saveAll(orderItemEntityList);

        //상품 재고 차감
        List<ProductEntity> productEntityList = new ArrayList<>();
        cartItemEntityList.forEach(cartItem -> {
            ProductEntity productEntity = cartItem.getProductEntity();
            productEntity.decreaseStock(cartItem.getQuantity());
            productEntityList.add(productEntity);
        });
        this.productRepository.saveAll(productEntityList);

        //장바구니 상품 삭제
        this.cartItemRepository.deleteAll(cartItemEntityList);
    }
}
