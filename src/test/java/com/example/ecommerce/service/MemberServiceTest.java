package com.example.ecommerce.service;

import com.example.ecommerce.domain.MemberEntity;
import com.example.ecommerce.exception.impl.NotExistsAccountException;
import com.example.ecommerce.exception.impl.NotExistsTokenException;
import com.example.ecommerce.exception.impl.NotMatchPasswordException;
import com.example.ecommerce.exception.impl.TokenIsEmptyException;
import com.example.ecommerce.model.auth.Auth;
import com.example.ecommerce.model.auth.LogIn;
import com.example.ecommerce.model.auth.RefreshToken;
import com.example.ecommerce.repository.MemberRepository;
import com.example.ecommerce.repository.RefreshTokenRepository;
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
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @InjectMocks
    private MemberService memberService;

    private static final String TOKEN_PREFIX = "Bearer";
    private static final String VALID_TOKEN = "validToken";
    private static final String VALID_REFRESH_TOKEN = TOKEN_PREFIX + VALID_TOKEN;

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

        given(memberRepository.existsByNameAndPhone("you", "01012341234"))
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
        NotMatchPasswordException exception = assertThrows(NotMatchPasswordException.class, () ->
                memberService.logIn(member));

        //then
        assertEquals("비밀번호가 일치하지 않습니다", exception.getMessage());
        assertEquals(400, exception.getStatusCode());
    }

    @Test
    @DisplayName("새 Access토큰 발급")
    void getNewTokenSuccess() {
        //given
        Long userId = 1L;
        RefreshToken tokenAndUserId = new RefreshToken(VALID_TOKEN, userId);

        List<String> role = Arrays.asList("SELLER", "CLIENT");

        MemberEntity memberEntity = MemberEntity.builder()
                .id(1L)
                .name("you")
                .password("qwe123")
                .phone("01012341234")
                .address("부산광역시")
                .role(role)
                .build();

        given(refreshTokenRepository.findById(VALID_TOKEN))
                .willReturn(Optional.of(tokenAndUserId));
        given(memberRepository.findById(tokenAndUserId.getUserId()))
                .willReturn(Optional.of(memberEntity));

        //when
        LogIn.Response response = memberService.getNewToken(VALID_REFRESH_TOKEN);

        //then
        assertEquals("you", response.getName());
        assertEquals(role, response.getRoles());
        assertEquals(1L, response.getUserId());
    }

    @Test
    @DisplayName("새 Access토큰 발급 실패_토큰이 비어있음")
    void getNewTokenFail_TokenIsEmpty() {
        //when
        assertThrows(TokenIsEmptyException.class, ()
                -> memberService.getNewToken(null));

        assertThrows(TokenIsEmptyException.class, ()
                -> memberService.getNewToken(""));

        assertThrows(TokenIsEmptyException.class, ()
                -> memberService.getNewToken(VALID_TOKEN));

    }

    @Test
    @DisplayName("새 Access토큰 발급 실패_토큰으로 아이디 찾기 실패")
    void getNewTokenFail_RefreshTokenRepository_NotFoundById() {
        //given
        given(refreshTokenRepository.findById(VALID_TOKEN))
                .willReturn(Optional.empty());

        //when
        assertThrows(NotExistsTokenException.class, ()
                -> memberService.getNewToken(VALID_REFRESH_TOKEN));

        //refreshTokenRepository.findById 메서드 호출이 이루어졌는지 검증
        verify(refreshTokenRepository).findById(VALID_TOKEN);
    }

    @Test
    @DisplayName("새 Access토큰 발급 실패_memberEntity 찾기 실패")
    void getNewTokenFail_MemberRepository_NotFoundById() {
        //given
        Long userId = 1L;
        RefreshToken tokenAndUserId = new RefreshToken(VALID_TOKEN, userId);

        given(refreshTokenRepository.findById(VALID_TOKEN))
                .willReturn(Optional.of(tokenAndUserId));
        given(memberRepository.findById(tokenAndUserId.getUserId()))
                .willReturn(Optional.empty());

        //when
        assertThrows(NotExistsAccountException.class, ()
                -> memberService.getNewToken(VALID_REFRESH_TOKEN));

        //refreshTokenRepository.findById 메서드 호출이 이루어졌는지 검증
        verify(refreshTokenRepository).findById(VALID_TOKEN);
        verify(memberRepository).findById(tokenAndUserId.getUserId());
    }

    @Test
    @DisplayName("회원 탈퇴 성공")
    void withdrawalSuccess() {
        //given
        Auth.Withdrawal member = new Auth.Withdrawal("you", "qwe123");

        List<String> role = Arrays.asList("SELLER", "CLIENT");

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
        String response = memberService.withdrawal(member);

        //then
        assertEquals("회원 탈퇴가 정상적으로 완료되었습니다", response);
        verify(memberRepository).deleteByName(memberEntity.getName());
    }

    @Test
    @DisplayName("회원 탈퇴 실패_회원 정보 없음")
    void withdrawalFail_NoExistsMemberEntity() {
        //given
        Auth.Withdrawal member = new Auth.Withdrawal("you", "qwe123");

        given(memberRepository.findByName(member.getName()))
                .willReturn(Optional.empty());

        //when
        assertThrows(NotExistsAccountException.class, ()
                -> memberService.withdrawal(member));

        //then
        verify(memberRepository).findByName(member.getName());
    }

    @Test
    @DisplayName("회원 탈퇴 실패_비밀번호 매치 실패")
    void withdrawalFail_NoMatchPassword() {
        //given
        Auth.Withdrawal member = new Auth.Withdrawal("you", "qwe122");

        List<String> role = Arrays.asList("SELLER", "CLIENT");

        MemberEntity memberEntity = MemberEntity.builder()
                .id(1L)
                .name("you")
                .password("qwe123")
                .phone("01012341234")
                .address("부산광역시")
                .role(role)
                .build();

        given(memberRepository.findByName(member.getName()))
                .willReturn(Optional.of(memberEntity));
        given(passwordEncoder.matches(member.getPassword(), memberEntity.getPassword()))
                .willReturn(false);

        //when
        assertThrows(NotMatchPasswordException.class, () ->
                memberService.withdrawal(member));

        //then
        verify(passwordEncoder).matches(member.getPassword(), memberEntity.getPassword());
    }
}