package com.example.glibrary.service;

import org.springframework.stereotype.Service;
import com.example.glibrary.exception.TooManyRequestsException;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RateLimiterService {

    private static final int MAX_REQUESTS_PER_SECOND = 50;

    private Instant currentSecond = Instant.now();
    private final AtomicInteger requestCount = new AtomicInteger(0);

    public synchronized void validateRequestRate() {
        Instant now = Instant.now();

        if (now.getEpochSecond() != currentSecond.getEpochSecond()) {
            // Новая секунда — сбрасываем счётчик
            currentSecond = now;
            requestCount.set(0);
        }

        if (requestCount.incrementAndGet() > MAX_REQUESTS_PER_SECOND) {
            throw new TooManyRequestsException("Слишком много запросов. Попробуйте позже.");
        }
    }
}
