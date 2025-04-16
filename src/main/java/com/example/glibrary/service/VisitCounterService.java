package com.example.glibrary.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class VisitCounterService {

    private final Map<String, AtomicInteger> characterVisitCounter = new ConcurrentHashMap<>();

    public int incrementCharacterVisit(String characterName) {
        return characterVisitCounter
                .computeIfAbsent(characterName, k -> new AtomicInteger(0))
                .incrementAndGet();
    }

    public int getCharacterVisitCount(String characterName) {
        return characterVisitCounter.getOrDefault(characterName, new AtomicInteger(0)).get();
    }
}
