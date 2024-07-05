package com.example.ecommerce.repository;

import com.example.ecommerce.domain.ProductEntity;
import com.example.ecommerce.domain.ProductImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImageEntity, Long> {

    void deleteAllByProductEntity(ProductEntity productEntity);
}
