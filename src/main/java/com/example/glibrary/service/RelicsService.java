package com.example.glibrary.service;

import com.example.glibrary.DTO.RelicsDto;
import com.example.glibrary.exception.NotFoundException;
import com.example.glibrary.model.Character;
import com.example.glibrary.model.Relic;
import com.example.glibrary.repository.CharacterRepository;
import com.example.glibrary.repository.RelicsRepository;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class RelicsService {

    private final RelicsRepository relicsRepository;
    private final CharacterRepository characterRepository;
    private final Map<String, RelicsDto> cache = new ConcurrentHashMap<>();

    public RelicsService(RelicsRepository relicsRepository, CharacterRepository characterRepository) {
        
        this.relicsRepository = relicsRepository;
        this.characterRepository = characterRepository;
    }

    public Relic createRelic(RelicsDto relicsDto) {

        Relic relic = new Relic();

        relic.setName(relicsDto.getName());
        relic.setType(relicsDto.getType());
        relic.setPcs2(relicsDto.getPcs2());
        relic.setPcs4(relicsDto.getPcs4());
        relic.setRarity(relicsDto.getRarity());

        return relicsRepository.save(relic);
    }

    public List<RelicsDto> readRelics() {
        List<RelicsDto> relics = relicsRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        if (relics.isEmpty()) {
            throw new NotFoundException("List of Relics is empty");
        }

        return relics;
    }

    public RelicsDto readRelicsByName(String relicsName) {
        if (cache.containsKey(relicsName)) {
            return cache.get(relicsName);  // Берем из кэша
        }

        return relicsRepository.findByName(relicsName)
                .map(relics -> {
                    RelicsDto dto = toDto(relics);  // Конвертируем в DTO
                    cache.put(relicsName, dto);     // Кладем в кэш
                    return dto;
                })
                .orElseThrow(() -> new NotFoundException("Relic '" + relicsName + "' not found"));
    }


    public Relic updateRelic(String relicsName, RelicsDto relicsDto) {
        return relicsRepository.findByName(relicsName)
                .map(relics -> {
                    relics.setName(relicsDto.getName());
                    relics.setType(relicsDto.getType());
                    relics.setPcs2(relicsDto.getPcs2());
                    relics.setPcs4(relicsDto.getPcs4());
                    relics.setRarity(relicsDto.getRarity());
                    return relicsRepository.save(relics);
                })
                .orElseThrow(() -> new NotFoundException("Relic with name " + relicsName + " not found"));
    }

    public Relic updateRelicCharacter(String relicsName, String characterName) {
        Character character = characterRepository.findByName(characterName)
                .orElseThrow(() -> new NotFoundException("Character with name " + characterName + " not found"));

        Relic relic = relicsRepository.findByName(relicsName)
                .orElseThrow(() -> new NotFoundException("Relic with name " + relicsName + " not found"));

        character.getRecRelics().add(relic);
        relic.getCharacters().add(character);

        characterRepository.save(character);
        return relicsRepository.save(relic);
    }

    @Transactional
    public void deleteRelic(String relicName) {
        Relic relic = relicsRepository.findByName(relicName)
                .orElseThrow(() -> new NotFoundException("Relic with name " + relicName + " not found"));

        // Удаляем реликвию из всех связанных персонажей
        relic.getCharacters().forEach(character -> character.getRecRelics().remove(relic));

        relicsRepository.delete(relic);
    }


    private RelicsDto toDto(Relic relic) {
        RelicsDto dto = new RelicsDto();

        dto.setId(relic.getId());
        dto.setName(relic.getName());
        dto.setType(relic.getType());
        dto.setPcs2(relic.getPcs2());
        dto.setPcs4(relic.getPcs4());
        dto.setRarity(relic.getRarity());

        // Получаем идентификаторы связанных персонажей
        if (relic.getCharacters() != null) {
            dto.setCharacterId(relic.getCharacters().stream()
                    .map(Character::getId) // Берем только ID персонажей
                    .collect(Collectors.toSet()));
        }

        return dto;
    }
}

