package com.example.ecommerce.service;

import com.example.ecommerce.domain.MemberEntity;
import com.example.ecommerce.model.Auth;
import com.example.ecommerce.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("회원가입 성공")
    void registerSuccess() {
        //given
        List<String> role = Arrays.asList("SELLER", "CLIENT");

        Auth.Register member = Auth.Register.builder()
                .name("you")
                .password("qwe123")
                .phone("01012341234")
                .address("부산광역시")
                .roles(role)
                .build();

        when(this.memberRepository.save(any()))
                .thenReturn(MemberEntity.builder()
                        .name("you")
                        .password("qwe123")
                        .build());
        //when
        MemberEntity memberEntity = this.memberService.register(member);

        //then
        assertEquals("you", memberEntity.getName());
        assertEquals("qwe123", memberEntity.getPassword());
    }

    @Test
    @DisplayName("회원가입 실패")
    void registerFail() {
        //given

        //when
        //then
    }
}