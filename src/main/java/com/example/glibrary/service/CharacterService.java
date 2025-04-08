package com.example.glibrary.service;

import com.example.glibrary.DTO.CharacterDto;
import com.example.glibrary.DTO.CharacterWithRelicsDto;
import com.example.glibrary.model.GameCharacter;
import com.example.glibrary.model.GameRelics;
import com.example.glibrary.repository.CharacterRepository;
import com.example.glibrary.repository.RelicsRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CharacterService {

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private RelicsRepository relicsRepository;

    private CharacterDto toDto(GameCharacter character) {
        CharacterDto dto = new CharacterDto();
        dto.setId(character.getId());
        dto.setName(character.getName());
        dto.setType(character.getType());
        dto.setRole(character.getRole());
        dto.setWeapon(character.getWeapon());
        dto.setRarity(character.getRarity());

        if (character.getRecRelics() != null) {
            dto.setRelicIds(character.getRecRelics().stream()
                    .map(GameRelics::getId)
                    .collect(Collectors.toSet()));
        }

        return dto;
    }

    public CharacterDto createCharacter(CharacterDto dto) {
        GameCharacter character = new GameCharacter();
        character.setName(dto.getName());
        character.setType(dto.getType());
        character.setRole(dto.getRole());
        character.setWeapon(dto.getWeapon());
        character.setRarity(dto.getRarity());

        if (dto.getRelicIds() != null) {
            Set<GameRelics> relics = new HashSet<>(relicsRepository.findAllById(dto.getRelicIds()));
            character.setRecRelics(relics);
        }

        return toDto(characterRepository.save(character));
    }

    public CharacterDto addRelicsToCharacter(String characterName, Set<Long> relicIds) {
        GameCharacter character = characterRepository.findByName(characterName)
                .orElseThrow(() -> new EntityNotFoundException("Character not found with name: "
                        + characterName));

        Set<GameRelics> relics = new HashSet<>(relicsRepository.findAllById(relicIds));

        character.getRecRelics().addAll(relics);

        characterRepository.save(character);

        return toDto(character);
    }

    public List<CharacterDto> readCharacters() {
        return characterRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public CharacterDto readCharacter(String name) {
        GameCharacter character = characterRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Character not found with name: "
                        + name));
        return toDto(character);
    }

    public CharacterDto updateCharacter(String name, CharacterDto dto) {
        GameCharacter character = characterRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Character not found with name: "
                        + name));

        character.setName(dto.getName());
        character.setType(dto.getType());
        character.setRole(dto.getRole());
        character.setWeapon(dto.getWeapon());
        character.setRarity(dto.getRarity());

        if (dto.getRelicIds() != null) {
            Set<GameRelics> relics = new HashSet<>(relicsRepository.findAllById(dto.getRelicIds()));
            character.setRecRelics(relics);
        }

        return toDto(characterRepository.save(character));
    }

    public void deleteCharacter(String name) {
        GameCharacter character = characterRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Character not found with name: "
                        + name));
        characterRepository.delete(character);
    }

    public CharacterWithRelicsDto getCharacterWithRelics(String characterName) {
        GameCharacter character = characterRepository.findByName(characterName)
                .orElseThrow(() -> new EntityNotFoundException("Character not found with name: "
                        + characterName));

        CharacterWithRelicsDto result = new CharacterWithRelicsDto();
        result.setCharacterName(character.getName());

        Set<String> relicNames = character.getRecRelics().stream()
                .map(GameRelics::getRName)
                .collect(Collectors.toSet());

        result.setRelicNames(relicNames);

        return result;
    }
}
