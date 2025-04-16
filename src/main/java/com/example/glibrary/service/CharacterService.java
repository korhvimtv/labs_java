package com.example.glibrary.service;

import com.example.glibrary.DTO.CharacterDto;
import com.example.glibrary.DTO.RegionDto;
import com.example.glibrary.DTO.RelicsDto;
import com.example.glibrary.cache.CharacterCache;
import com.example.glibrary.model.Character;
import com.example.glibrary.model.Relic;
import com.example.glibrary.repository.CharacterRepository;
import jakarta.persistence.EntityNotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CharacterService {

    private final CharacterRepository characterRepository;
    private final CharacterCache characterCache;

    public CharacterService(CharacterRepository characterRepository, CharacterCache characterCache) {

        this.characterRepository = characterRepository;
        this.characterCache = characterCache;
    }

    public Character createCharacter(CharacterDto characterDto) {

        Character character = new Character();

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

        Optional<CharacterDto> cachedCharacter = characterCache.get(characterName);
        if (cachedCharacter.isPresent()) {
            return cachedCharacter;
        }

        return characterRepository.findByName(characterName)
                .map(character -> {
                    CharacterDto dto = toDto(character);
                    characterCache.put(characterName, dto);
                    return dto;
                });
    }

    public List<CharacterDto> readCharacterByTypeAndRole(String type, String role) {

        return characterRepository.findByTypeAndRole(type, role).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<CharacterDto> findCharactersByRegionName(String regionName) {
        return characterRepository.findByRegionName(regionName).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Character updateCharacter(String characterName, CharacterDto characterDto) {
        return characterRepository.findByName(characterName)
                .map(character -> {
                    character.setName(characterDto.getName());
                    character.setType(characterDto.getType());
                    character.setRole(characterDto.getRole());
                    character.setWeapon(characterDto.getWeapon());
                    character.setRarity(characterDto.getRarity());

                    Character updatedCharacter = characterRepository.save(character);

                    if (characterCache.get(characterName).isPresent()) {
                        characterCache.put(characterName, toDto(updatedCharacter));
                    }

                    return updatedCharacter;
                }).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public void deleteCharacter(String characterName) {

        Character character = characterRepository.findByName(characterName)
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

        characterCache.remove(characterName);
    }

    private CharacterDto toDto(Character character) {
        CharacterDto dto = new CharacterDto();

        dto.setId(character.getId());
        dto.setName(character.getName());
        dto.setType(character.getType());
        dto.setRole(character.getRole());
        dto.setWeapon(character.getWeapon());
        dto.setRarity(character.getRarity());

        // Преобразование Relic в RelicsDto
        Set<RelicsDto> relicsDtos = new HashSet<>();
        if (character.getRecRelics() != null) {
            for (Relic relic : character.getRecRelics()) {
                RelicsDto relicDto = new RelicsDto();
                relicDto.setId(relic.getId());
                relicDto.setName(relic.getName());
                relicDto.setType(relic.getType());
                relicDto.setPcs2(relic.getPcs2());
                relicDto.setPcs4(relic.getPcs4());
                relicDto.setRarity(relic.getRarity());
                relicsDtos.add(relicDto);
            }
        }
        dto.setRelic(relicsDtos);

        // Преобразование Region в RegionDto
        Set<RegionDto> regionDtos = new HashSet<>();
        if (character.getRegion() != null) {
            RegionDto regionDto = new RegionDto();
            regionDto.setId(character.getRegion().getId());
            regionDto.setName(character.getRegion().getName());
            regionDto.setArchon(character.getRegion().getArchon());
            regionDtos.add(regionDto);
        }
        dto.setRegion(regionDtos);

        return dto;
    }
}
