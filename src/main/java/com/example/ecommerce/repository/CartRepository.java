package com.example.ecommerce.repository;

import com.example.ecommerce.domain.CartEntity;
import com.example.ecommerce.domain.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {

    Optional<CartEntity> findByMemberEntity(MemberEntity memberEntity);

    boolean existsByMemberEntity(MemberEntity memberEntity);
}
