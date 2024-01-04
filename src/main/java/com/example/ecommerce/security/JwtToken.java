package com.example.ecommerce.security;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtToken {
    private static final String TOKEN_PREFIX = "Bearer";

    @Value("{spring.jwt.secret}")
    private String secretKey;

    public String getUserName(String totalToken) {
        String token = totalToken.substring(TOKEN_PREFIX.length());

        return Jwts.parser()
                .setSigningKey(this.secretKey)
                .parseClaimsJws(token).getBody().getSubject();
    }


}
