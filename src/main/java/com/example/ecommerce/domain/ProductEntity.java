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

    @Setter
    private LocalDateTime modifiedDate;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;
}
