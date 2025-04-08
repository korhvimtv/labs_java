package com.example.glibrary.service;

import com.example.glibrary.DTO.CharacterDto;
import com.example.glibrary.DTO.RegionDto;
import com.example.glibrary.model.GameCharacter;
import com.example.glibrary.model.GameRegion;
import com.example.glibrary.model.GameRelics;
import com.example.glibrary.repository.CharacterRepository;
import com.example.glibrary.repository.RegionRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RegionService {

    private final RegionRepository regionRepository;
    private final CharacterRepository characterRepository;

    @Autowired
    public RegionService(RegionRepository regionRepository,
                         CharacterRepository characterRepository) {
        this.regionRepository = regionRepository;
        this.characterRepository = characterRepository;
    }

    public RegionDto createRegion(RegionDto dto) {
        GameRegion region = new GameRegion();
        region.setRegionName(dto.getRegionName());
        region.setArchonRegion(dto.getArchonRegion());

        return toDto(regionRepository.save(region));
    }

    public RegionDto updateRegion(Long id, RegionDto dto) {
        GameRegion region = regionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Регион не найден!"));

        region.setRegionName(dto.getRegionName());
        region.setArchonRegion(dto.getArchonRegion());

        return toDto(regionRepository.save(region));
    }

    public List<RegionDto> readRegions() {
        return regionRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public RegionDto readRegion(Long id) {
        GameRegion region = regionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Регион не найден!"));
        return toDto(region);
    }

    public void deleteRegion(Long id) {
        regionRepository.deleteById(id);
    }

    public CharacterDto assignCharacterToRegion(Long characterId, Long regionId) {
        GameCharacter character = characterRepository.findById(characterId)
                .orElseThrow(() -> new EntityNotFoundException("Персонаж не найден!"));
        GameRegion region = regionRepository.findById(regionId)
                .orElseThrow(() -> new EntityNotFoundException("Регион не найден!"));

        character.setRegion(region);
        return toDto(characterRepository.save(character));
    }

    private RegionDto toDto(GameRegion region) {
        RegionDto dto = new RegionDto();
        dto.setId(region.getId());
        dto.setRegionName(region.getRegionName());
        dto.setArchonRegion(region.getArchonRegion());

        if (region.getCharacters() != null) {
            dto.setCharacters(region.getCharacters().stream()
                    .map(this::toDto)
                    .collect(Collectors.toSet()));
        }

        return dto;
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

        return dto;
    }
}