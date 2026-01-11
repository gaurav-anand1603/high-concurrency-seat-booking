package com.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LockService {

    private final StringRedisTemplate redisTemplate;

    public String lockSeat(String key) {
        // Prevent concurrency
        String lockValue = UUID.randomUUID().toString();
        // TTL to prevent deadlock
        Boolean taken = redisTemplate.opsForValue()
                .setIfAbsent(key, lockValue, Duration.ofSeconds(10));

        return Boolean.TRUE.equals(taken) ? lockValue : null;
    }

    public void releaseLock(String key, String lockValue){
        String redisLockVal = redisTemplate.opsForValue().get(key);
        // Only owner can delete.
        if(lockValue.equals(redisLockVal)){
            redisTemplate.delete(key);
        }
    }

}
