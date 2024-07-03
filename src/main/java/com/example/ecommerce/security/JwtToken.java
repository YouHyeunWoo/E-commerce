package com.example.ecommerce.security;

import com.example.ecommerce.domain.MemberEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtToken {
    public MemberEntity getMemberEntityFromAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (MemberEntity) authentication.getPrincipal();
    }
}
