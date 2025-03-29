package com.example.vitabuddy.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RefreshService {

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${jwt.refresh-expiration-ms}")
    private long refreshTokenExpirationMs; // 예: 86400000L (24시간)

    public RefreshService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Redis에 refresh 토큰 저장 (key: refreshToken, value: userId)
    public void saveRefreshToken(String userId, String refreshToken) {
        redisTemplate.opsForValue().set(refreshToken, userId, Duration.ofMillis(refreshTokenExpirationMs));
    }

    // 해당 refreshToken이 존재하는지 확인
    public boolean existsByRefresh(String refreshToken) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(refreshToken));
    }

    // refreshToken 삭제
    public void deleteByRefresh(String refreshToken) {
        redisTemplate.delete(refreshToken);
    }

    // 저장된 refreshToken으로부터 userId 조회 (필요 시 사용)
    public String getUserIdFromRefresh(String refreshToken) {
        return redisTemplate.opsForValue().get(refreshToken);
    }
}
