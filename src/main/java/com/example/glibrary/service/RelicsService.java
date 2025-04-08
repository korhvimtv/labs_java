package com.example.glibrary.service;

import com.example.glibrary.DTO.RelicsDto;
import com.example.glibrary.model.GameCharacter;
import com.example.glibrary.model.GameRelics;
import com.example.glibrary.repository.CharacterRepository;
import com.example.glibrary.repository.RelicsRepository;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class RelicsService {

    private final RelicsRepository relicsRepository;
    private final CharacterRepository characterRepository;

    public RelicsService(RelicsRepository relicsRepository, CharacterRepository characterRepository) {
        
        this.relicsRepository = relicsRepository;
        this.characterRepository = characterRepository;
    }

    public GameRelics createRelic(RelicsDto relicsDto) {

        GameRelics relic = new GameRelics();

        relic.setRName(relicsDto.getRName());
        relic.setRType(relicsDto.getRType());
        relic.setR2pcs(relicsDto.getR2pcs());
        relic.setR4pcs(relicsDto.getR4pcs());
        relic.setRRarity(relicsDto.getRRarity());

        return relicsRepository.save(relic);
    }

    public List<RelicsDto> readRelics() {

        return relicsRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Optional<RelicsDto> readRelicsByName(String relicsName) {

        return relicsRepository.findByrName(relicsName)
                .map(this::toDto);
    }

    public GameRelics updateRelic(String relicsName, RelicsDto relicsDto) {

        return relicsRepository.findByrName(relicsName)
                .map (relics -> {
                    relics.setRName(relicsDto.getRName());
                    relics.setRType(relicsDto.getRType());
                    relics.setR2pcs(relicsDto.getR2pcs());
                    relics.setR4pcs(relicsDto.getR4pcs());
                    relics.setRRarity(relicsDto.getRRarity());
                    return relicsRepository.save(relics);
                }).orElseThrow(EntityNotFoundException::new);
    }

    public GameRelics updateRelicCharacter(String relicsName, String characterName) {

        GameCharacter character = characterRepository.findByName(characterName)
                .orElseThrow(EntityNotFoundException::new);

        GameRelics relics = relicsRepository.findByrName(relicsName)
                .orElseThrow(EntityNotFoundException::new);

        character.getRecRelics().add(relics);
        relics.getCharacters().add(character);

        characterRepository.save(character);
        return relicsRepository.save(relics);
    }

    @Transactional
    public void deleteRelic(String relicName) {
        GameRelics relics = relicsRepository.findByrName(relicName)
                .orElseThrow(() -> new EntityNotFoundException("Relic with name " + relicName + " not found"));

        // Удаляем реликвию из всех связанных персонажей
        relics.getCharacters().forEach(character -> character.getRecRelics().remove(relics));

        relicsRepository.delete(relics);
    }

    private RelicsDto toDto(GameRelics relics) {
        RelicsDto dto = new RelicsDto();

        dto.setId(relics.getId());
        dto.setRName(relics.getRName());
        dto.setRType(relics.getRType());
        dto.setR2pcs(relics.getR2pcs());
        dto.setR4pcs(relics.getR4pcs());
        dto.setRRarity(relics.getRRarity());

        // Получаем идентификаторы связанных персонажей
        if (relics.getCharacters() != null) {
            dto.setCharacterIds(relics.getCharacters().stream()
                    .map(GameCharacter::getId) // Берем только ID персонажей
                    .collect(Collectors.toSet()));
        }

        return dto;
    }
}

