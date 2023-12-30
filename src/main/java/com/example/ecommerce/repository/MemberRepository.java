package com.example.ecommerce.repository;

import com.example.ecommerce.domain.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByName(String name);

    boolean existsByName(String name);

    boolean existsByPhone(String phoneNumber);

    @Transactional
    void deleteByName(String userName);
}
