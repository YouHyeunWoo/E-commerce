package com.example.ecommerce.service;

import com.example.ecommerce.domain.MemberEntity;
import com.example.ecommerce.model.Auth;
import com.example.ecommerce.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    //회원가입
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.memberRepository.findByName(username)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 아이디 입니다"));
    }

    public MemberEntity register(Auth.Register register) {
        boolean existsName = this.memberRepository.existsByName(register.getName());
        boolean existsPhoneNumber = this.memberRepository.existsByPhone(register.getPhone());
        if (existsName) {
            throw new RuntimeException("존재하는 아이디 입니다");
        }
        if(existsPhoneNumber){
            throw new RuntimeException("존재하는 핸드폰번호 입니다");
        }
        register.setPassword(passwordEncoder.encode(register.getPassword()));
        return this.memberRepository.save(register.toEntity());
    }
}
