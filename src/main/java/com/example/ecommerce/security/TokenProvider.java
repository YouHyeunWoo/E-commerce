package com.example.ecommerce.security;

import com.example.ecommerce.service.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TokenProvider {
    private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60;
    private static final String KEY_ROLES = "roles";
    private final MemberService memberService;

    @Value("{spring.jwt.secret}")
    private String secretKey;

    public String generateToken(String name, List<String> roles) {
        //jwt는 원하는 정보를 담기 위해 Claim이라는 공간을 제공해 준다.
        //Claim은 일종의 Map자료구조처럼 키, 밸류 쌍으로 정보를 넣을 수 있다
        Claims claims = Jwts.claims().setSubject(name);
        claims.put(KEY_ROLES, roles);

        //현재시간과 만료시간 설정 >> 만료시간은 현재시간에서 한시간 후
        var now = new Date();
        var expireDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now) //토큰 생성 시간
                .setExpiration(expireDate) //토큰 만료 시간
                .signWith(SignatureAlgorithm.HS512, this.secretKey) //토큰생성 알고리즘, 비밀키
                .compact();
    }

    //토큰의 subject(사용자 이름) 가져오기
    private String getName(String token) {
        return this.parseClaims(token).getSubject();
        //jwt토큰의 body 부분에서 claim의 subject로 넣어줬던 이름을 가져옴
    }

    public Authentication getAuthentication(String jwt) {
        UserDetails userDetails =
                this.memberService.loadUserByUsername(this.getName(jwt));

        return new UsernamePasswordAuthenticationToken(userDetails, ""
                , userDetails.getAuthorities());
    }

    //토큰의 유효성검사
    public boolean validateToken(String token) {
        //토큰이 비어있지 않은지 확인
        if (!StringUtils.hasText(token)) {
            return false;
        }
        var claims = this.parseClaims(token);
        //토큰의 만료 시간이 지났는지 확인
        //만료 시간이 현재 시간보다 이전인지 (만료됨)
        return !claims.getExpiration().before(new Date());
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token).getBody();
        //jwt토큰은 header.body.signature로 이루어져있다. 그 중 body부분을 가져옴
    }
}
