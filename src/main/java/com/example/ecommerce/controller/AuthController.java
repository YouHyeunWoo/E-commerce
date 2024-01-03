package com.example.ecommerce.controller;

import com.example.ecommerce.domain.MemberEntity;
import com.example.ecommerce.model.Auth;
import com.example.ecommerce.security.TokenProvider;
import com.example.ecommerce.service.MemberService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Slf4j
public class AuthController {
    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    //회원가입 API
    @PostMapping("/auth/account")
    public ResponseEntity<?> register(@Valid @RequestBody Auth.Register request) {
        MemberEntity memberEntity = this.memberService.register(request);

        return ResponseEntity.ok(memberEntity);
    }
    @RequestMapping("/auth")
    public ResponseEntity<?> logIn(@RequestBody Auth.LogIn request) {
        MemberEntity memberEntity = this.memberService.logIn(request);
        //로그인이 정상적으로 완료되면 토큰 발행
        String token = this.tokenProvider
                .generateToken(memberEntity.getName(), memberEntity.getRole());

        return ResponseEntity.ok(token);
    }

    @DeleteMapping("/auth/account")
    public ResponseEntity<?> withdrawal(@RequestBody Auth.Withdrawal request){
        return ResponseEntity.ok(this.memberService.withdrawal(request));
    }

}
