package com.example.glibrary.cache;

import com.example.glibrary.DTO.CharacterDto;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CharacterCache {

    private static final int MAX_CACHE_SIZE = 100;

    private final Map<String, CharacterDto> cache = Collections.synchronizedMap(new LinkedHashMap<String, CharacterDto>(16, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, CharacterDto> eldest) {
            return size() > MAX_CACHE_SIZE;
        }
    });

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