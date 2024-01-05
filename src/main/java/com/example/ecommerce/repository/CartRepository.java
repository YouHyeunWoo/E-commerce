package com.example.ecommerce.repository;

import com.example.ecommerce.domain.CartEntity;
import com.example.ecommerce.domain.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {
    List<CartEntity> findAllByMemberEntity(MemberEntity memberEntity);
}
