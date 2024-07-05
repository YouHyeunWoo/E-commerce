package com.example.ecommerce.repository;

import com.example.ecommerce.domain.MemberEntity;
import com.example.ecommerce.domain.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    boolean existsByProductNameAndMemberEntity(String productName, MemberEntity memberEntity);

    Optional<ProductEntity> findByProductNameAndMemberEntity(String productName, MemberEntity memberEntity);

    Optional<ProductEntity> findByProductIdAndMemberEntity(Long productId, MemberEntity memberEntity);

    List<ProductEntity> findAllByMemberEntity(MemberEntity memberEntity);

    Optional<ProductEntity> findByProductId(Long productId);

    //날짜순 내림차순(최근에 등록된 날짜 순으로) 정렬을 위한 query
    @Query("select m from product m where m.productName = :productName order by m.registerDate desc")
    Page<ProductEntity> findAllByProductName(String productName, Pageable pageable);

    @Transactional
    void deleteByProductIdAndMemberEntity(Long productId, MemberEntity memberEntity);
}
