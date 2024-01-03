package com.example.ecommerce.security;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;

public class JwtToken {
    private static final String TOKEN_PREFIX = "Bearer";
    @Value("{spring.jwt.secret}")
    private static String secretKey;

    public static String getUserName(String totalToken){
        String token = totalToken.substring(TOKEN_PREFIX.length());

        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token).getBody().getSubject();
    }
}
