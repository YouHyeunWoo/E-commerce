package com.example.ecommerce.service;

import com.example.ecommerce.domain.MemberEntity;
import com.example.ecommerce.domain.ProductEntity;
import com.example.ecommerce.domain.ProductImageEntity;
import com.example.ecommerce.exception.impl.AlreadyExistsSameProductException;
import com.example.ecommerce.exception.impl.NotExistsProductException;
import com.example.ecommerce.model.product.DeleteProduct;
import com.example.ecommerce.model.product.GetProduct;
import com.example.ecommerce.model.product.ModifyProduct;
import com.example.ecommerce.model.product.RegisterProduct;
import com.example.ecommerce.repository.ProductImageRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.security.JwtToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final JwtToken jwtToken;
    private final S3Service s3Service;
    private final ProductImageRepository productImageRepository;

    //상품 등록
    //판매자의 아이디가 존재하는지 확인 >> 존재하면 상품 등록 가능
    //판매자는 자신이 등록한 동일한 이름의 상품은 등록 불가
    //상품의 이미지 저장
    @Transactional
    public RegisterProduct.Response registerProduct(RegisterProduct.Registration product,
                                                    MultipartFile multipartFile) {
        MemberEntity memberEntity = this.jwtToken.getMemberEntityFromAuthentication();

        boolean exists = this.productRepository.existsByProductNameAndMemberEntity(
                product.getProductName(), memberEntity);

        if (exists) {
            throw new AlreadyExistsSameProductException();
        }

        ProductEntity productEntity = this.productRepository.save(ProductEntity.builder()
                .memberEntity(memberEntity)
                .productName(product.getProductName())
                .price(product.getPrice())
                .stock(product.getStock())
                .explanation(product.getExplanation())
                .registerDate(LocalDateTime.now())
                .build());

        String imageUrl = this.s3Service.upload(multipartFile);
        this.productImageRepository.save(ProductImageEntity.builder()
                .imageUrl(imageUrl)
                .productEntity(productEntity)
                .uploadDate(LocalDateTime.now())
                .build());

        return RegisterProduct.Response.from(productEntity, imageUrl);
    }

    //상품 검색(판매자용)
    //판매자가 등록한 상품을 모두 검색하는 기능 >> 등록된 상품을 리스트로 만들어 반환
    public List<GetProduct.Seller> searchProduct() {
        MemberEntity memberEntity = this.jwtToken.getMemberEntityFromAuthentication();

        List<ProductEntity> productEntityList =
                this.productRepository.findAllByMemberEntity(memberEntity);

        if (productEntityList.isEmpty()) {
            throw new NotExistsProductException();
        }

        return productEntityList.stream().map(product -> GetProduct.Seller.builder()
                .productId(product.getProductId())
                .price(product.getPrice())
                .stock(product.getStock())
                .explanation(product.getExplanation())
                .imageUrlList(product.getProductImageEntityList().stream()
                        .map(ProductImageEntity::getImageUrl)
                        .collect(Collectors.toList()))
                .build()).collect(Collectors.toList());
    }

    //상품 검색 >> 로그인 하지 않은 사용자도 이용 가능
    //상품의 이름을 입력하여 검색 >> 상품의 이름과 일치하는 모든 상품을 리스트로 만들어 반환
    //가장 최신에 등록된 상품 순으로 검색
    public List<GetProduct.Client> searchByProductName(String productName, Pageable pageable) {
        Page<ProductEntity> productEntityList =
                this.productRepository.findAllByProductName(productName, pageable);

        if (productEntityList.isEmpty()) {
            throw new NotExistsProductException();
        }

        return productEntityList.stream().map(product -> GetProduct.Client.builder()
                .productId(product.getProductId())
                .price(product.getPrice())
                .stock(product.getStock())
                .explanation(product.getExplanation())
                .sellerName(product.getMemberEntity().getName())
                .sellerPhone(product.getMemberEntity().getPhone())
                .registerDate(product.getRegisterDate())
                .modifiedDate(product.getModifiedDate())
                .imageUrlList(product.getProductImageEntityList().stream()
                        .map(ProductImageEntity::getImageUrl)
                        .collect(Collectors.toList()))
                .build()).collect(Collectors.toList());
    }

    //상품 수정(판매자용)
    //상품 상품의 이름과 그 상품을 등록한 판매자의 아이디가 일치하는지 확인
    //상품 update는 ProductEntity 내부에 updateProduct 메소드를 만들어 update진행
    @Transactional
    public ProductEntity modifyProduct(ModifyProduct.Request request) {
        MemberEntity memberEntity = jwtToken.getMemberEntityFromAuthentication();

        ProductEntity productEntity = this.productRepository
                .findByProductNameAndMemberEntity(request.getProductName(), memberEntity)
                .orElseThrow(NotExistsProductException::new);

        productEntity.updateProduct(request.getPrice(), request.getStock(), request.getExplanation());

        return this.productRepository.save(productEntity);
    }

    //상품 삭제
    //판매자 아이디와 상품 이름으로 상품이 존재하는지 확인
    //상품 삭제 시 이미지테이블에 상품과 연관된 이미지도 삭제
    //S3버킷에서 이미지 삭제
    //존재하면 상품 삭제
    @Transactional
    public DeleteProduct.Response deleteProduct(Long productId) {
        MemberEntity memberEntity = jwtToken.getMemberEntityFromAuthentication();

        ProductEntity productEntity = this.productRepository.findByProductIdAndMemberEntity
                (productId, memberEntity).orElseThrow(NotExistsProductException::new);

        List<String> productImageUrlList
                = productEntity.getProductImageEntityList().stream()
                .map(ProductImageEntity::getImageUrl)
                .toList();

        //상품 이미지를 S3 버킷에서 삭제
        this.s3Service.deleteImage(productImageUrlList);

        //상품 이미지 삭제
        this.productImageRepository.deleteAllByProductEntity(productEntity);

        //상품 삭제
        this.productRepository.deleteByProductIdAndMemberEntity(productId, memberEntity);

        return DeleteProduct.Response.builder()
                .productName(productEntity.getProductName())
                .sellerName(memberEntity.getName())
                .build();
    }
}
