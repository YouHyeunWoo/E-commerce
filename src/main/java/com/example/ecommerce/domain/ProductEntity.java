package com.example.ecommerce.domain;

import jakarta.persistence.*;
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
    private Long productId;

    private String productName;

    private Long price;

    private String explanation;

    private Long amount;

    private LocalDateTime registerDate;

    private LocalDateTime modifiedDate;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    public void updateProduct(Long price, Long amount, String explanation) {
        this.amount = amount;
        this.price = price;
        this.explanation = explanation;
        this.modifiedDate = LocalDateTime.now();
    }
}
