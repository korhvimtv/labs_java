package com.example.glibrary.service;

import com.example.glibrary.DTO.RelicWithCharactersDto;
import com.example.glibrary.DTO.RelicsDto;
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
public class RelicsService {

    @Autowired
    private RelicsRepository relicsRepository;

    @Autowired
    private CharacterRepository characterRepository;

    public RelicsDto createRelic(RelicsDto relicDto) {
        // Проверяем, что rName и другие поля приходят в запросе
        System.out.println("Received Relic DTO: " + relicDto);

        GameRelics relic = new GameRelics();
        relic.setRName(relicDto.getRName());
        relic.setRType(relicDto.getRType());
        relic.setR2pcs(relicDto.getR2pcs());
        relic.setR4pcs(relicDto.getR4pcs());
        relic.setRRarity(relicDto.getRRarity());

        // Если characterIds не передается, не добавляем их
        relic.setRecCharacters(new HashSet<>());  // пустой Set для characterIds

        GameRelics savedRelic = relicsRepository.save(relic);
        System.out.println("Saving Relic Entity: " + relic);
        return toDto(savedRelic); // Возвращаем DTO после сохранения
    }

    // Получение всех реликвий
    public List<RelicsDto> readRelics() {
        return relicsRepository.findAll().stream()
                .map(this::toDto) // Преобразуем сущности в DTO
                .collect(Collectors.toList());
    }

    // Получение реликвии по имени
    public RelicsDto readRelic(String rName) {
        GameRelics relic = relicsRepository.findByrName(rName)
                .orElseThrow(() -> new EntityNotFoundException("Relic not found with name: "
                        + rName));
        return toDto(relic);
    }

    // Обновление реликвии
    public RelicsDto updateRelic(GameRelics relic) {
        GameRelics existingRelic = relicsRepository.findByrName(relic.getRName())
                .orElseThrow(() -> new EntityNotFoundException("Relic not found with name: "
                        + relic.getRName()));

        // Обновляем данные реликвии
        existingRelic.setRName(relic.getRName());
        existingRelic.setRType(relic.getRType());
        existingRelic.setR2pcs(relic.getR2pcs());
        existingRelic.setR4pcs(relic.getR4pcs());
        existingRelic.setRRarity(relic.getRRarity());

        GameRelics updatedRelic = relicsRepository.save(existingRelic);
        return toDto(updatedRelic); // Возвращаем обновленное DTO
    }

    // Удаление реликвии
    public void deleteRelic(String rName) {
        GameRelics relic = relicsRepository.findByrName(rName)
                .orElseThrow(() -> new EntityNotFoundException("Relic not found with name: "
                        + rName));
        relicsRepository.delete(relic);
    }

    public RelicsDto addCharactersToRelic(String relicName, Set<String> characterName) {
        // Находим артефакт по имени
        GameRelics relic = relicsRepository
                .findByrName(relicName)
                .orElseThrow(() -> new EntityNotFoundException("Relic not found with name: "
                        + relicName));

        // Ищем персонажей по именам
        Set<GameCharacter> characters = new HashSet<>(characterRepository
                .findByNameIn(characterName));

        // Добавляем персонажей к артефакту
        relic.getRecCharacters().addAll(characters);

        // Сохраняем изменения
        relicsRepository.save(relic);

        return toDto(relic);
    }

    public RelicWithCharactersDto getRelicWithCharacters(String relicName) {
        GameRelics relic = relicsRepository
                .findByrName(relicName)
                .orElseThrow(() -> new EntityNotFoundException("Relic not found with name: "
                        + relicName));

        // Создаем DTO с артефактом и его персонажами
        RelicWithCharactersDto result = new RelicWithCharactersDto();
        result.setRelicName(relic.getRName());

        Set<String> characterNames = relic.getRecCharacters().stream()
                .map(GameCharacter::getName)  // Получаем имена персонажей
                .collect(Collectors.toSet());

        result.setCharacterNames(characterNames);

        return result;
    }

    // Преобразование сущности GameRelics в DTO
    private RelicsDto toDto(GameRelics relic) {
        RelicsDto dto = new RelicsDto();
        dto.setId(relic.getId());
        dto.setRName(relic.getRName());
        dto.setRType(relic.getRType());
        dto.setR2pcs(relic.getR2pcs());
        dto.setR4pcs(relic.getR4pcs());
        dto.setRRarity(relic.getRRarity());

        // Сет персонажей, связанных с реликвией
        Set<Long> characterIds = relic.getRecCharacters().stream()
                .map(GameCharacter::getId)
                .collect(Collectors.toSet());
        dto.setCharacterIds(characterIds);

        return dto;
    }
}

