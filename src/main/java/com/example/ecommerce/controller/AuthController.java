package com.example.ecommerce.controller;

import com.example.ecommerce.model.Auth;
import com.example.ecommerce.model.LogIn;
import com.example.ecommerce.model.RefreshToken;
import com.example.ecommerce.model.Token;
import com.example.ecommerce.repository.RefreshTokenRepository;
import com.example.ecommerce.security.TokenProvider;
import com.example.ecommerce.service.MemberService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Slf4j
public class AuthController {
    private final MemberService memberService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    //회원가입 API
    @PostMapping("/auth/account")
    public Auth.RegisterResponse register(@Valid @RequestBody Auth.Register request) {
        return this.memberService.register(request);
    }

    @RequestMapping("/auth")
    public Token logIn(@RequestBody LogIn.Request request) {
        LogIn.Response logIn = this.memberService.logIn(request);
        Token token = this.tokenProvider.generateToken(logIn.getName(), logIn.getRoles());
        this.refreshTokenRepository.save(
                new RefreshToken(token.getRefreshToken(), logIn.getUserId()));

        return token;
    }

    @GetMapping("/auth/token")
    public Token getNewToken(@RequestHeader(value = "Authorization") String oldToken){
        LogIn.Response logIn = this.memberService.getNewToken(oldToken);

        Token newToken = this.tokenProvider.generateToken(logIn.getName(), logIn.getRoles());
        this.refreshTokenRepository.save(
                new RefreshToken(newToken.getRefreshToken(), logIn.getUserId()));

        return newToken;
    }

    @DeleteMapping("/auth/account")
    public ResponseEntity<?> withdrawal(@RequestBody Auth.Withdrawal request) {
        return ResponseEntity.ok(this.memberService.withdrawal(request));
    }
}
