package com.example.ecommerce.controller;

import com.example.ecommerce.domain.MemberEntity;
import com.example.ecommerce.model.Auth;
import com.example.ecommerce.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {
    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Auth.Register request){
        MemberEntity memberEntity = this.memberService.register(request);

        return ResponseEntity.ok(memberEntity);
    }
}
