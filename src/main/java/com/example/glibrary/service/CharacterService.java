package com.example.glibrary.service;

import com.example.glibrary.DTO.CharacterDto;
import com.example.glibrary.model.GameCharacter;
import com.example.glibrary.model.GameRegion;
import com.example.glibrary.model.GameRelics;
import com.example.glibrary.repository.CharacterRepository;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CharacterService {

    private final CharacterRepository characterRepository;

    public CharacterService(CharacterRepository characterRepository) {

        this.characterRepository = characterRepository;
    }

    public GameCharacter createCharacter(CharacterDto characterDto) {

        GameCharacter character = new GameCharacter();

        character.setName(characterDto.getName());
        character.setType(characterDto.getType());
        character.setRole(characterDto.getRole());
        character.setWeapon(characterDto.getWeapon());
        character.setRarity(characterDto.getRarity());

        return characterRepository.save(character);
    }

    public List<CharacterDto> readCharacters() {

        return characterRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Optional<CharacterDto> readCharacterByName(String characterName) {

        return characterRepository.findByName(characterName)
                .map(this::toDto);
    }

    public GameCharacter updateCharacter(String characterName, CharacterDto characterDto) {

        return characterRepository.findByName(characterName)
                .map(character -> {
                    character.setName(characterDto.getName());
                    character.setType(characterDto.getType());
                    character.setRole(characterDto.getRole());
                    character.setWeapon(characterDto.getWeapon());
                    character.setRarity(characterDto.getRarity());
                    return characterRepository.save(character);
                }).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public void deleteCharacter(String characterName) {

        GameCharacter character = characterRepository.findByName(characterName)
                .orElseThrow(() -> new EntityNotFoundException("Character with name " + characterName + " not found"));

        if (character.getRecRelics() != null) {
            character.getRecRelics().forEach(relic -> relic.getCharacters().remove(character));
            character.getRecRelics().clear();
        }

        if (character.getRegion() != null) {
            character.getRegion().getCharacters().remove(character);
            character.setRegion(null);
        }

        characterRepository.delete(character);
    }

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

        if (character.getRegion() != null) {
            dto.setRegionNames(Set.of(character.getRegion().getRegionName())); // Добавляем название региона в Set
        }

        return dto;
    }
}
