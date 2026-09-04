package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class RateLimitService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RateLimitService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private final Map<String, Integer> requestCounts = new ConcurrentHashMap<>();
    private static final int MAX_REQUESTS_PER_MINUTE = 60;

    public boolean isAllowed(String identifier) {
        String key = "rate_limit:" + identifier;
        
        try {
            // Используем Redis для rate limiting
            Long count = redisTemplate.opsForValue().increment(key);
            
            if (count != null && count == 1) {
                // Устанавливаем TTL для ключа (1 минута)
                redisTemplate.expire(key, Duration.ofMinutes(1));
            }
            
            if (count != null && count > MAX_REQUESTS_PER_MINUTE) {
                log.warn("Rate limit exceeded for: {}", identifier);
                return false;
            }
            
            return true;
        } catch (Exception e) {
            log.error("Error checking rate limit for: {}", identifier, e);
            // В случае ошибки разрешаем запрос
            return true;
        }
    }

    public boolean isAllowedWithRedlock(String identifier) {
        // TODO: Реализовать Redlock алгоритм для распределенной блокировки
        // Это упрощенная версия
        return isAllowed(identifier);
    }

    public int getRemainingRequests(String identifier) {
        String key = "rate_limit:" + identifier;
        Object count = redisTemplate.opsForValue().get(key);
        
        if (count != null) {
            return Math.max(0, MAX_REQUESTS_PER_MINUTE - Integer.parseInt(count.toString()));
        }
        return MAX_REQUESTS_PER_MINUTE;
    }
}
