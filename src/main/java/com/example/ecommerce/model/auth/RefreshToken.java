package com.example.ecommerce.model.auth;

import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "refreshToken", timeToLive = 3600)
@Getter
public class RefreshToken {
    @Id
    private final String refreshToken;
    private final Long userId;

    public RefreshToken(String refreshToken, Long userId) {
        this.refreshToken = refreshToken;
        this.userId = userId;
    }
}
