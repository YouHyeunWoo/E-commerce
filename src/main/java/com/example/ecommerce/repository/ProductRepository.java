package com.example.ecommerce.repository;

import com.example.ecommerce.domain.MemberEntity;
import com.example.ecommerce.domain.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    boolean existsByProductNameAndMemberEntity(String productName, MemberEntity memberEntity);

    List<ProductEntity> findAllByMemberEntity(MemberEntity memberEntity);

    @Transactional
    void deleteByProductNameAndMemberEntity(String productName, MemberEntity memberEntity);
}
