package com.example.ecommerce.service;

import com.example.ecommerce.domain.MemberEntity;
import com.example.ecommerce.exception.impl.AlreadyExistsAccount;
import com.example.ecommerce.exception.impl.AlreadyExistsPhoneNumber;
import com.example.ecommerce.exception.impl.NotExistsAccount;
import com.example.ecommerce.exception.impl.NotMatchPassword;
import com.example.ecommerce.model.Auth;
import com.example.ecommerce.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    //회원가입
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.memberRepository.findByName(username)
                .orElseThrow(NotExistsAccount::new);
    }

    public MemberEntity register(Auth.Register register) {
        boolean existsName = this.memberRepository.existsByName(register.getName());
        boolean existsPhoneNumber = this.memberRepository.existsByPhone(register.getPhone());
        if (existsName) {
            throw new AlreadyExistsAccount();
        }
        if (existsPhoneNumber) {
            throw new AlreadyExistsPhoneNumber();
        }
        register.setPassword(passwordEncoder.encode(register.getPassword()));
        return this.memberRepository.save(register.toEntity());
    }

    //로그인 메소드
    //입력받은 아이디 비밀번호로 계정이 존재하는지, 비밀번호가 일치하는지 확인
    public MemberEntity logIn(Auth.LogIn member) {
        MemberEntity memberEntity = getMemberEntity(member.getName());
        matchPassword(member.getPassword(), memberEntity);
        return memberEntity;
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
        MemberEntity memberEntity = this.memberRepository.findByName(member)
                .orElseThrow(NotExistsAccount::new);
        return memberEntity;
    }

    private void matchPassword(String password, MemberEntity memberEntity) {
        if (!passwordEncoder.matches(password, memberEntity.getPassword())) {
            throw new NotMatchPassword();
        }
    }
}
