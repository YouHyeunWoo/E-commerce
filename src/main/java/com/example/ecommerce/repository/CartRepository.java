package com.example.ecommerce.repository;

import com.example.ecommerce.domain.CartEntity;
import com.example.ecommerce.domain.MemberEntity;
import com.example.ecommerce.domain.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {
    List<CartEntity> findAllByMemberEntity(MemberEntity memberEntity);

    boolean existsByProductEntity(ProductEntity productEntity);

    @Transactional
    void deleteByProductEntityAndMemberEntity(ProductEntity productEntity, MemberEntity memberEntity);
}
