package com.example.ecommerce.repository;

import com.example.ecommerce.domain.CartEntity;
import com.example.ecommerce.domain.CartItemEntity;
import com.example.ecommerce.domain.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {
    boolean existsByProductEntityAndCartEntity(ProductEntity productEntity, CartEntity cartEntity);

    List<CartItemEntity> findAllByCartEntity(CartEntity cartEntity);

    Optional<CartItemEntity> findByCartEntityAndProductEntity(CartEntity cartEntity, ProductEntity productEntity);

    void deleteByCartEntityAndProductEntity(CartEntity cartEntity, ProductEntity productEntity);

    void deleteAllByCartEntity(CartEntity cartEntity);

}
