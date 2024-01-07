package com.example.ecommerce.service;

import com.example.ecommerce.domain.MemberEntity;
import com.example.ecommerce.domain.ProductEntity;
import com.example.ecommerce.model.DeleteProduct;
import com.example.ecommerce.model.GetProduct;
import com.example.ecommerce.model.ModifyProduct;
import com.example.ecommerce.model.RegisterProduct;
import com.example.ecommerce.repository.MemberRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.security.JwtToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final JwtToken jwtToken;

    //상품 등록
    //판매자의 아이디가 존재하는지 확인 >> 존재하면 상품 등록 가능
    //판매자는 자신이 등록한 동일한 이름의 상품은 등록 불가
    public ProductEntity registerProduct(String totalToken, RegisterProduct.Registration product) {
        String userName = jwtToken.getUserName(totalToken);

        MemberEntity memberEntity = this.getMemberEntity(userName);

        boolean exists = this.productRepository.existsByProductNameAndMemberEntity(
                product.getProductName(), memberEntity);

        if (exists) {
            throw new RuntimeException("동일한 이름의 상품이 이미 등록되어 있습니다.");
        }

        return productRepository.save(product.toEntity(memberEntity));
    }

    //상품 검색(판매자용)
    //판매자가 등록한 상품을 모두 검색하는 기능 >> 등록된 상품을 리스트로 만들어 반환
    public List<GetProduct.Seller> searchProduct(String totalToken) {
        String userName = jwtToken.getUserName(totalToken);

        MemberEntity memberEntity = this.getMemberEntity(userName);

        List<ProductEntity> productEntityList =
                this.productRepository.findAllByMemberEntity(memberEntity);

        if (productEntityList.isEmpty()) {
            throw new RuntimeException("등록된 상품이 없습니다");
        }

        return productEntityList.stream().map(e -> new GetProduct.Seller(e.getProductId(),
                        e.getPrice(), e.getAmount(), e.getExplanation()))
                .collect(Collectors.toList());
    }

    //상품 검색 >> 로그인 하지 않은 사용자도 이용 가능
    //상품의 이름을 입력하여 검색 >> 상품의 이름과 일치하는 모든 상품을 리스트로 만들어 반환
    //가장 최신에 등록된 상품 순으로 검색
    public List<GetProduct.Client> searchByProductName(String productName, Pageable pageable) {
        Page<ProductEntity> productEntityList =
                this.productRepository.findAllByProductName(productName, pageable);

        if (productEntityList.isEmpty()) {
            throw new RuntimeException("등록된 상품이 없습니다");
        }

        return productEntityList.stream().map(e -> new GetProduct.Client(e.getProductId(),
                        e.getPrice(), e.getAmount(), e.getExplanation(),
                        e.getMemberEntity().getName(),
                        e.getMemberEntity().getPhone(),
                        e.getRegisterDate(),
                        e.getModifiedDate()))
                .collect(Collectors.toList());
    }

    //상품 수정(판매자용)
    //상품 상품의 이름과 그 상품을 등록한 판매자의 아이디가 일치하는지 확인
    //상품 update는 ProductEntity 내부에 updateProduct 메소드를 만들어 update진행
    @Transactional
    public ProductEntity modifyProduct(String totalToken, ModifyProduct.Request request) {
        String userName = jwtToken.getUserName(totalToken);

        MemberEntity memberEntity = this.getMemberEntity(userName);

        ProductEntity productEntity = this.productRepository
                .findByProductNameAndMemberEntity(request.getProductName(), memberEntity)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 상품 입니다"));

        productEntity.updateProduct(request.getPrice(), request.getAmount(), request.getExplanation());

        return this.productRepository.save(productEntity);
    }

    //상품 삭제
    //판매자 아이디와 상품 이름으로 상품이 존재하는지 확인
    //존재하면 상품 삭제
    public DeleteProduct.Response deleteProduct(String totalToken, DeleteProduct.Request removeProduct) {
        String userName = jwtToken.getUserName(totalToken);

        MemberEntity memberEntity = this.getMemberEntity(userName);

        boolean existsProduct = this.productRepository
                .existsByProductNameAndMemberEntity(
                        removeProduct.getProductName(), memberEntity);

        if (!existsProduct) {
            throw new RuntimeException("해당 판매자가 판매하는 상품이 아닙니다");
        }

        this.productRepository.deleteByProductNameAndMemberEntity(
                removeProduct.getProductName(), memberEntity);

        return DeleteProduct.Response.builder()
                .productName(removeProduct.getProductName())
                .sellerName(userName)
                .build();
    }

    private MemberEntity getMemberEntity(String userName) {
        return this.memberRepository.findByName(userName)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 아이디 입니다."));
    }
}
