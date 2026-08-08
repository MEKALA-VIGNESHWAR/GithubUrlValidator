package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisLockService {

    private static final Logger log = LoggerFactory.getLogger(RedisLockService.class);

    private final StringRedisTemplate redisTemplate;

    public RedisLockService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean acquireLock(String lockKey, String lockValue, Duration expireTime) {
        try {
            Boolean acquired = redisTemplate.opsForValue().setIfAbsent("lock:" + lockKey, lockValue, expireTime);
            return Boolean.TRUE.equals(acquired);
        } catch (Exception e) {
            log.warn("Redis lock acquisition failed for key {}: {}", lockKey, e.getMessage());
            return true; // Fallback to allowing execution if Redis down locally
        }
    }

    public void releaseLock(String lockKey, String lockValue) {
        try {
            String currentVal = redisTemplate.opsForValue().get("lock:" + lockKey);
            if (lockValue.equals(currentVal)) {
                redisTemplate.delete("lock:" + lockKey);
            }
        } catch (Exception e) {
            log.warn("Redis lock release failed for key {}: {}", lockKey, e.getMessage());
        }
    }
}
