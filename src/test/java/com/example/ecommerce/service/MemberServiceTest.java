package com.example.ecommerce.service;

import com.example.ecommerce.domain.MemberEntity;
import com.example.ecommerce.exception.impl.NotMatchPassword;
import com.example.ecommerce.model.Auth;
import com.example.ecommerce.model.LogIn;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;

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

        //passwordEncoder 가짜 이므로 이렇게 하면 encodedPassword가 null이 나온다
        //String encodedPassword = passwordEncoder.encode(member.getPassword());

        String encodedPassword = "encodedPassword";

        MemberEntity savedMember = MemberEntity.builder()
                .id(1L)
                .name(member.getName())
                .password(encodedPassword)
                .phone(member.getPhone())
                .address(member.getAddress())
                .role(member.getRoles())
                .build();

        given(memberRepository.existsByName("you"))
                .willReturn(false);
        given(memberRepository.existsByPhone("01012341234"))
                .willReturn(false);
        given(passwordEncoder.encode(member.getPassword()))
                .willReturn(encodedPassword);
        given(memberRepository.save(argThat(user ->
                user.getName().equals("you") &&
                        user.getPhone().equals("01012341234") &&
                        user.getPassword().equals(encodedPassword) &&
                        user.getAddress().equals("부산광역시") &&
                        user.getRole().equals(role))))
                .willReturn(savedMember);

        //when
        Auth.RegisterResponse auth = memberService.register(member);

        //then
        assertEquals("you", auth.getName());
        assertEquals("01012341234", auth.getPhone());
        assertEquals(role, auth.getRoles());
    }

    @Test
    @DisplayName("로그인 성공")
    void logInSuccess() {
        //given
        List<String> role = Arrays.asList("SELLER", "CLIENT");

        LogIn.Request member = LogIn.Request.builder()
                .name("you")
                .password("qwe123")
                .build();

        MemberEntity memberEntity = MemberEntity.builder()
                .id(1L)
                .name("you")
                .password("qwe123")
                .phone("01012341234")
                .address("부산광역시")
                .role(role)
                .build();

        given(memberRepository.findByName("you"))
                .willReturn(Optional.of(memberEntity));
        given(passwordEncoder.matches("qwe123", "qwe123"))
                .willReturn(true);

        //when
        LogIn.Response logIn = memberService.logIn(member);

        //then
        assertEquals("you", logIn.getName());
        assertEquals(role, logIn.getRoles());
    }

    @Test
    @DisplayName("로그인 실패")
    void logInFail() {
        //given
        List<String> role = Arrays.asList("SELLER", "CLIENT");

        LogIn.Request member = LogIn.Request.builder()
                .name("you")
                .password("qwe122")
                .build();

        MemberEntity memberEntity = MemberEntity.builder()
                .id(1L)
                .name("you")
                .password("qwe123")
                .phone("01012341234")
                .address("부산광역시")
                .role(role)
                .build();

        given(memberRepository.findByName("you"))
                .willReturn(Optional.of(memberEntity));
        given(passwordEncoder.matches("qwe122", "qwe123"))
                .willReturn(false);

        //when
        NotMatchPassword exception = assertThrows(NotMatchPassword.class, () ->
                memberService.logIn(member));

        //then
        assertEquals("비밀번호가 일치하지 않습니다", exception.getMessage());
        assertEquals(400, exception.getStatusCode());
    }
}