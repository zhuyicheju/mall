package com.example.mall.manager;

import java.util.concurrent.ConcurrentHashMap;

import com.google.common.util.concurrent.RateLimiter;

public class UserRateLimiterManager {
    private static final double PERMITS_PER_SECOND = 1.0;

    private final ConcurrentHashMap<Long, RateLimiter> limiterMap = new ConcurrentHashMap<>();

    public boolean tryAcquire(Long userId) {
        RateLimiter limiter = limiterMap.computeIfAbsent(
            userId,
            id -> RateLimiter.create(PERMITS_PER_SECOND)
        );
        return limiter.tryAcquire();
    }
}
