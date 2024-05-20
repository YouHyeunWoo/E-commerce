package com.example.ecommerce.repository;

import com.example.ecommerce.model.auth.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {
    private static final long REFRESH_TOKEN_LIFE = 1000 * 60 * 60 * 3;

    private final RedisTemplate<String, Long> redisTemplate;

    public void save(final RefreshToken refreshToken) {
        ValueOperations<String, Long> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(refreshToken.getRefreshToken(), refreshToken.getUserId());
        redisTemplate.expire(refreshToken.getRefreshToken(), REFRESH_TOKEN_LIFE, TimeUnit.SECONDS);
    }

    public Optional<RefreshToken> findById(final String refreshToken){
        ValueOperations<String, Long> valueOperations = redisTemplate.opsForValue();
        Long userId = valueOperations.get(refreshToken);

        if(Objects.isNull(userId)){
            return Optional.empty();
        }
        return Optional.of(new RefreshToken(refreshToken, userId));
    }

}
