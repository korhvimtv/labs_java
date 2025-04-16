package com.example.glibrary.service;

import com.example.glibrary.DTO.CharacterDto;
import com.example.glibrary.exception.NotFoundException;
import com.example.glibrary.DTO.RegionDto;
import com.example.glibrary.DTO.RelicsDto;
import com.example.glibrary.cache.CharacterCache;
import com.example.glibrary.model.Character;
import com.example.glibrary.model.Region;
import com.example.glibrary.model.Relic;
import com.example.glibrary.repository.CharacterRepository;

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
    private final VisitCounterService visitCounterService;
    private final RateLimiterService rateLimiterService;

    public CharacterService(CharacterRepository characterRepository, CharacterCache characterCache,
                            VisitCounterService visitCounterService, RateLimiterService rateLimiterService) {

        this.characterRepository = characterRepository;
        this.characterCache = characterCache;
        this.visitCounterService = visitCounterService;
        this.rateLimiterService = rateLimiterService;
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

    public List<CharacterDto> createBulkCharacters(List<CharacterDto> characterDtos) {
        List<Character> characters = characterDtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());

        List<Character> savedCharacters = characterRepository.saveAll(characters);

        return savedCharacters.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<CharacterDto> readCharacters() {

        List<CharacterDto> characters = characterRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        if (characters.isEmpty()) {
            throw new NotFoundException("List of Characters is empty");
        }

        return characters;
    }

    public CharacterDto readCharacterByName(String characterName) {

        rateLimiterService.validateRequestRate();

        visitCounterService.incrementCharacterVisit(characterName);

        Optional<CharacterDto> cachedCharacter = characterCache.get(characterName);
        if (cachedCharacter.isPresent()) {
            return cachedCharacter.get();
        }

        return characterRepository.findByName(characterName)
                .map(character -> {
                    CharacterDto dto = toDto(character);
                    characterCache.put(characterName, dto);
                    return dto;
                })
                .orElseThrow(() -> new NotFoundException("Character '" + characterName + "' not found"));
    }

    public List<CharacterDto> readCharacterByTypeAndRole(String type, String role) {
        List<CharacterDto> characters = characterRepository.findByTypeAndRole(type, role).stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        if (characters.isEmpty()) {
            throw new NotFoundException("Character with type '" + type + "' and role '" + role + "' not found");
        }

        return characters;
    }

    public List<CharacterDto> findCharactersByRegionName(String regionName) {
        List<CharacterDto> characters = characterRepository.findByRegionName(regionName).stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        if (characters.isEmpty()) {
            throw new NotFoundException("Characters from '" + regionName + "' not found");
        }

        return characters;
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
                }).orElseThrow(() -> new NotFoundException("Character '" + characterName + "' not found"));
    }

    @Transactional
    public void deleteCharacter(String characterName) {
        Character character = characterRepository.findByName(characterName)
                .orElseThrow(() -> new NotFoundException("Character '" + characterName + "' not found"));

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

    private Character toEntity(CharacterDto dto) {

        Character character = new Character();

        character.setId(dto.getId());
        character.setName(dto.getName());
        character.setType(dto.getType());
        character.setRole(dto.getRole());
        character.setWeapon(dto.getWeapon());
        character.setRarity(dto.getRarity());

        Set<Relic> relics = new HashSet<>();
        if (dto.getRelic() != null) {
            for (RelicsDto relicDto : dto.getRelic()) {
                Relic relic = new Relic();
                relic.setId(relicDto.getId());
                relic.setName(relicDto.getName());
                relic.setType(relicDto.getType());
                relic.setPcs2(relicDto.getPcs2());
                relic.setPcs4(relicDto.getPcs4());
                relic.setRarity(relicDto.getRarity());
                relics.add(relic);
            }
        }
        character.setRecRelics(relics);

        if (dto.getRegion() != null && !dto.getRegion().isEmpty()) {
            RegionDto regionDto = dto.getRegion().iterator().next();
            Region region = new Region();
            region.setId(regionDto.getId());
            region.setName(regionDto.getName());
            region.setArchon(regionDto.getArchon());
            character.setRegion(region);
        }

        return character;
    }

}
