package com.example.ecommerce.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "product")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private String productName;

    private Long price;

    private String explanation;

    private Long amount;

    private LocalDateTime registerDate;

    private LocalDateTime modifiedDate;
}
