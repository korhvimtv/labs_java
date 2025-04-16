package com.example.glibrary.cache;

import com.example.glibrary.DTO.CharacterDto;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CharacterCache {
    private final Map<String, CharacterDto> cache = new ConcurrentHashMap<>();

    public Optional<CharacterDto> get(String name) {
        return Optional.ofNullable(cache.get(name));
    }

    public void put(String name, CharacterDto dto) {
        cache.put(name, dto);
    }

    public void remove(String name) {
        cache.remove(name);
    }
}