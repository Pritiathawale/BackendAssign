package com.assignment.BackendIntern.service;

import com.assignment.BackendIntern.constant.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class TokenCacheService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String BLACKLIST_PREFIX = "blacklist:";

    // ✅ Blacklist a token when user logs out
    public void blacklistToken(String token) {
        try {
            String key = BLACKLIST_PREFIX + token;
            redisTemplate.opsForValue().set(
                key,
                "blacklisted",
                AppConstants.JWT_EXPIRY_MS,
                TimeUnit.MILLISECONDS
            );
            log.info("Token blacklisted successfully in Redis");
        } catch (Exception e) {
            log.error("Failed to blacklist token in Redis: {}", e.getMessage());
        }
    }

    //  token is blacklisted before allowing request
    public boolean isTokenBlacklisted(String token) {
        try {
            String key = BLACKLIST_PREFIX + token;
            boolean blacklisted = Boolean.TRUE.equals(redisTemplate.hasKey(key));
            if (blacklisted) {
                log.warn("Attempt to use blacklisted token");
            }
            return blacklisted;
        } catch (Exception e) {
            
            log.error("Redis check failed: {} — allowing request", e.getMessage());
            return false;
        }
    }
}