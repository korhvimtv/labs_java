package com.example.glibrary.service;

import com.example.glibrary.DTO.CharacterDto;
import com.example.glibrary.DTO.RegionDto;
import com.example.glibrary.exception.NotFoundException;
import com.example.glibrary.model.Character;
import com.example.glibrary.model.Region;
import com.example.glibrary.repository.CharacterRepository;
import com.example.glibrary.repository.RegionRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class RegionService {

    private final RegionRepository regionRepository;
    private final CharacterRepository characterRepository;
    private final Map<String, RegionDto> cache = new ConcurrentHashMap<>();

    public RegionService(RegionRepository regionRepository, CharacterRepository characterRepository) {

        this.regionRepository = regionRepository;
        this.characterRepository = characterRepository;
    }

    public Region createRegion(RegionDto regionDto) {

        Region region = new Region();

        region.setName(regionDto.getName());
        region.setArchon(regionDto.getArchon());

        return regionRepository.save(region);
    }

    public List<RegionDto> readRegions() {
        List<RegionDto> regions = regionRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        if (regions.isEmpty()) {
            throw new NotFoundException("List of Regions is empty");
        }

        return regions;
    }

    public RegionDto readRegionByName(String regionName) {
        if (cache.containsKey(regionName)) {
            return cache.get(regionName);  // Берем из кэша
        }

        return regionRepository.findByName(regionName)
                .map(region -> {
                    RegionDto dto = toDto(region);  // Конвертируем в DTO
                    cache.put(regionName, dto);     // Кладем в кэш
                    return dto;
                })
                .orElseThrow(() -> new NotFoundException("Region '" + regionName + "' not found"));
    }


    public Region updateRegion(String regionName, RegionDto regionDto) {
        return regionRepository.findByName(regionName)
                .map(region -> {
                    region.setName(regionDto.getName());
                    region.setArchon(regionDto.getArchon());
                    return regionRepository.save(region);
                })
                .orElseThrow(() -> new NotFoundException("Region with name " + regionName + " not found"));
    }

    public RegionDto updateRegionCharacter(String characterName, String regionName) {
        Character character = characterRepository.findByName(characterName)
                .orElseThrow(() -> new NotFoundException("Character with name " + characterName + " not found"));

        Region region = regionRepository.findByName(regionName)
                .orElseThrow(() -> new NotFoundException("Region with name " + regionName + " not found"));

        character.setRegion(region);
        region.getCharacters().add(character);

        characterRepository.save(character);
        regionRepository.save(region);

        return toDto(region);
    }

    @Transactional
    public void deleteRegion(String regionName) {
        Region region = regionRepository.findByName(regionName)
                .orElseThrow(() -> new NotFoundException("Region with name " + regionName + " not found"));

        Set<Character> characters = region.getCharacters();

        characters.forEach(character -> {
            character.setRegion(null);
            characterRepository.save(character);
        });

        region.getCharacters().clear();

        regionRepository.delete(region);
    }


    private RegionDto toDto(Region region) {
        RegionDto dto = new RegionDto();

        dto.setId(region.getId());
        dto.setName(region.getName());
        dto.setArchon(region.getArchon());

        // Преобразуем Set<GameCharacter> в Set<CharacterDto>
        if (region.getCharacters() != null) {
            dto.setCharacters(region.getCharacters().stream()
                    .map(this::toCharacterDto) // Вызываем метод преобразования CharacterDto
                    .collect(Collectors.toSet()));
        }

        return dto;
    }

    private CharacterDto toCharacterDto(Character character) {
        CharacterDto dto = new CharacterDto();
        dto.setId(character.getId());
        dto.setName(character.getName());
        dto.setType(character.getType());
        dto.setRole(character.getRole());
        dto.setWeapon(character.getWeapon());
        dto.setRarity(character.getRarity());

        return dto;
    }
}