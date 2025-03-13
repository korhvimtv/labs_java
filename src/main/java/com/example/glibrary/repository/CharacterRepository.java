package com.example.glibrary.repository;

import com.example.glibrary.model.GameCharacter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterRepository extends JpaRepository<GameCharacter, Long> {
    static GameCharacter findByName(String name) {
        return null;
    }
}
