package com.example.ecommerce.service;

import com.example.ecommerce.repository.ProductImageRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.security.JwtToken;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private JwtToken jwtToken;
    @Mock
    private S3Service s3Service;
    @Mock
    private ProductImageRepository productImageRepository;

    @InjectMocks
    private ProductService productService;


}