package com.example.ecommerce.service;

import com.example.ecommerce.domain.CartEntity;
import com.example.ecommerce.model.Cart;
import com.example.ecommerce.repository.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CartService {
    private final CartRepository cartRepository;

    public CartEntity putIn(String totalToken, Cart.Request product){


    }
}
