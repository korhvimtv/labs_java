package com.example.glibrary.service;

import com.example.glibrary.model.GameCharacter;
import java.util.List;

import com.example.glibrary.repository.CharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CharacterService {

    @Autowired
    private CharacterRepository characterRepository;

    public GameCharacter createCharacter(GameCharacter character) {
        return characterRepository.save(character);
    }

    public List<GameCharacter> readCharacters() {
        return characterRepository.findAll();
    }

    public GameCharacter readCharacter(String name) {
        return CharacterRepository.findByName(name);
    }

    public GameCharacter updateCharacter(String name, GameCharacter characterData) {
        GameCharacter gameCharacter = CharacterRepository.findByName(name);

        gameCharacter.setName(characterData.getName());
        gameCharacter.setType(characterData.getType());
        gameCharacter.setRole(characterData.getRole());
        gameCharacter.setWeapon(characterData.getWeapon());
        gameCharacter.setRarity(characterData.getRarity());

        return characterRepository.save(gameCharacter);
    }

    public void deleteCharacter(String name) {
        GameCharacter gameCharacter = CharacterRepository.findByName(name);
        characterRepository.delete(gameCharacter);
    }
}
