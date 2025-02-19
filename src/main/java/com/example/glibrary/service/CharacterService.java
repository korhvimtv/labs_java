package com.example.glibrary.service;

import com.example.glibrary.model.GameCharacter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

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

    public List<GameCharacter> getCharactersByName(String name) {
        return characters.stream()
                .filter(character -> character.getName().equalsIgnoreCase(name))
                .toList();
    }

    public List<GameCharacter> getCharacterByWeaponAndType(String weapon, String type) {
        return characters.stream()
                .filter(character -> character.getWeapon().equalsIgnoreCase(weapon)
                        && character.getType().equalsIgnoreCase(type))
                .toList();
    }
}
