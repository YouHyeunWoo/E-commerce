package com.example.ecommerce.service;

import com.example.ecommerce.domain.MemberEntity;
import com.example.ecommerce.exception.impl.AlreadyExistsAccount;
import com.example.ecommerce.exception.impl.AlreadyExistsPhoneNumber;
import com.example.ecommerce.exception.impl.NotExistsAccount;
import com.example.ecommerce.exception.impl.NotMatchPassword;
import com.example.ecommerce.model.Auth;
import com.example.ecommerce.model.LogIn;
import com.example.ecommerce.model.RefreshToken;
import com.example.ecommerce.repository.MemberRepository;
import com.example.ecommerce.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService implements UserDetailsService {
    private static final String TOKEN_PREFIX = "Bearer";

    //회원가입
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.memberRepository.findByName(username)
                .orElseThrow(NotExistsAccount::new);
    }

    public Auth.RegisterResponse register(Auth.Register register) {
        boolean existsName = this.memberRepository.existsByName(register.getName());
        boolean existsPhoneNumber = this.memberRepository.existsByPhone(register.getPhone());
        if (existsName) {
            throw new AlreadyExistsAccount();
        }
        if (existsPhoneNumber) {
            throw new AlreadyExistsPhoneNumber();
        }
        register.setPassword(passwordEncoder.encode(register.getPassword()));
        return Auth.RegisterResponse.fromEntity(
                this.memberRepository.save(register.toEntity()));
    }

    //로그인 메소드
    //입력받은 아이디 비밀번호로 계정이 존재하는지, 비밀번호가 일치하는지 확인
    //로그인 시 access토큰과 refresh토큰을 발급
    public LogIn.Response logIn(LogIn.Request member) {
        MemberEntity memberEntity = getMemberEntity(member.getName());
        matchPassword(member.getPassword(), memberEntity);

        return LogIn.Response.fromEntity(memberEntity);
    }

    //기존 access토큰 만료 시 새로운 토큰 발급
    public LogIn.Response getNewToken(String refreshToken) {
        String token = refreshToken;
        if (!ObjectUtils.isEmpty(refreshToken) && refreshToken.startsWith(TOKEN_PREFIX)) {
            token = refreshToken.substring(TOKEN_PREFIX.length()).trim();
        }
        RefreshToken tokenAndUserId = this.refreshTokenRepository.findById(token)
                .orElseThrow(() -> new RuntimeException("Not Exists token"));

        MemberEntity memberEntity = this.memberRepository.findById(tokenAndUserId.getUserId())
                .orElseThrow(NotExistsAccount::new);

        return LogIn.Response.fromEntity(memberEntity);
    }

    //회원 탈퇴 메소드
    //입력받은 아이디가 존재하는지 확인 >> 비밀번호가 일치하는지 확인
    //일치하면 데이터 제거
    @Transactional
    public String withdrawal(Auth.Withdrawal member) {
        MemberEntity memberEntity = getMemberEntity(member.getName());
        matchPassword(member.getPassword(), memberEntity);
        this.memberRepository.deleteByName(memberEntity.getName());
        return "회원 탈퇴가 정상적으로 완료되었습니다";
    }

    private MemberEntity getMemberEntity(String member) {
        return this.memberRepository.findByName(member)
                .orElseThrow(NotExistsAccount::new);
    }

    private void matchPassword(String password, MemberEntity memberEntity) {
        if (!passwordEncoder.matches(password, memberEntity.getPassword())) {
            throw new NotMatchPassword();
        }
    }
}
