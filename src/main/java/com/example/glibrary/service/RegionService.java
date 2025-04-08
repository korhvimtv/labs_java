package com.example.glibrary.service;

import com.example.glibrary.DTO.CharacterDto;
import com.example.glibrary.DTO.RegionDto;
import com.example.glibrary.model.GameCharacter;
import com.example.glibrary.model.GameRegion;
import com.example.glibrary.repository.CharacterRepository;
import com.example.glibrary.repository.RegionRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class RegionService {

    private final RegionRepository regionRepository;
    private final CharacterRepository characterRepository;

    public RegionService(RegionRepository regionRepository, CharacterRepository characterRepository) {

        this.regionRepository = regionRepository;
        this.characterRepository = characterRepository;
    }

    public GameRegion createRegion(RegionDto regionDto) {

        GameRegion region = new GameRegion();

        region.setRegionName(regionDto.getRegionName());
        region.setArchonRegion(regionDto.getArchonRegion());

        return regionRepository.save(region);
    }

    public List<RegionDto> readRegions() {

        return regionRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Optional<RegionDto> readRegionByName(String regionName) {

        return regionRepository.findByRegionName(regionName)
                .map(this::toDto);
    }

    public GameRegion updateRegion(String regionName, RegionDto regionDto) {

        return regionRepository.findByRegionName(regionName)
                .map(region -> {
                    region.setRegionName(regionDto.getRegionName());
                    region.setArchonRegion(regionDto.getArchonRegion());
                    return regionRepository.save(region);
                }).orElseThrow(EntityNotFoundException::new);

    }

    public RegionDto updateRegionCharacter(String characterName, String regionName) {

        GameCharacter character = characterRepository.findByName(characterName)
                .orElseThrow(() -> new EntityNotFoundException("Character with name " + characterName + " not found"));

        GameRegion region = regionRepository.findByRegionName(regionName)
                .orElseThrow(() -> new EntityNotFoundException("Region with name " + regionName + " not found"));

        character.setRegion(region);
        region.getCharacters().add(character);

        characterRepository.save(character);
        regionRepository.save(region);

        return toDto(region); // Возвращаем DTO
    }

    @Transactional
    public void deleteRegion(String regionName) {
        GameRegion region = regionRepository.findByRegionName(regionName)
                .orElseThrow(() -> new EntityNotFoundException("Region with name " + regionName + " not found"));

        region.getCharacters().forEach(character -> character.setRegion(null));

        regionRepository.delete(region);
    }

    private RegionDto toDto(GameRegion region) {
        RegionDto dto = new RegionDto();

        dto.setId(region.getId());
        dto.setRegionName(region.getRegionName());
        dto.setArchonRegion(region.getArchonRegion());

        // Преобразуем Set<GameCharacter> в Set<CharacterDto>
        if (region.getCharacters() != null) {
            dto.setCharacters(region.getCharacters().stream()
                    .map(this::toCharacterDto) // Вызываем метод преобразования CharacterDto
                    .collect(Collectors.toSet()));
        }

        return dto;
    }

    private CharacterDto toCharacterDto(GameCharacter character) {
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