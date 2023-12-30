package com.example.ecommerce.domain;

import jakarta.persistence.*;
import lombok.*;

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
    private MemberEntity member;

    public void updateProduct(Long price, Long amount, String explanation){
        this.amount = amount;
        this.price = price;
        this.explanation = explanation;
        this.modifiedDate = LocalDateTime.now();
    }
}
