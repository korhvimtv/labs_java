package com.example.glibrary.service;

import com.example.glibrary.model.GameCharacter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CharacterService {

    private final List<GameCharacter> characters = new ArrayList<>();

    public boolean addCharacter(GameCharacter character) {
        characters.add(character);
        return true;
    }

    public List<GameCharacter> getAllCharacters() {
        return characters;
    }
}
