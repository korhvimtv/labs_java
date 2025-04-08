package com.example.glibrary.controller;

import com.example.glibrary.DTO.RelicWithCharactersDto;
import com.example.glibrary.DTO.RelicsDto;
import com.example.glibrary.model.GameRelics;
import com.example.glibrary.service.RelicsService;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/relics")
public class RelicsController {

    private final RelicsService relicsService;

    @Autowired
    public RelicsController(RelicsService relicsService) {
        this.relicsService = relicsService;
    }

    // Создание новой реликвии
    // Создание новой реликвии
    @PostMapping
    public ResponseEntity<RelicsDto> createRelic(@RequestBody RelicsDto relicDto) {
        RelicsDto createdRelic = relicsService.createRelic(relicDto);
        return new ResponseEntity<>(createdRelic, HttpStatus.CREATED);
    }

    // Получение всех реликвий
    @GetMapping
    public ResponseEntity<List<RelicsDto>> getAllRelics() {
        List<RelicsDto> relics = relicsService.readRelics();
        return ResponseEntity.ok(relics);
    }

    // Получение реликвии по имени
    @GetMapping("/{rName}")
    public ResponseEntity<RelicsDto> getRelicByName(@PathVariable String rName) {
        RelicsDto relic = relicsService.readRelic(rName);
        return ResponseEntity.ok(relic);
    }

    // Обновление реликвии
    @PutMapping("/{rName}")
    public ResponseEntity<RelicsDto> updateRelic(@RequestBody GameRelics relic) {
        RelicsDto updatedRelic = relicsService.updateRelic(relic);
        return ResponseEntity.ok(updatedRelic);
    }

    // Удаление реликвии
    @DeleteMapping("/{rName}")
    public ResponseEntity<Void> deleteRelic(@PathVariable String rName) {
        relicsService.deleteRelic(rName);
        return ResponseEntity.noContent().build();
    }

    // Добавление персонажей к артефакту по имени
    @PostMapping("/{name}/characters")
    public ResponseEntity<RelicsDto> addCharactersToRelic(
            @PathVariable String name,
            @RequestBody Set<String> characterNames) {

        RelicsDto updatedRelic = relicsService.addCharactersToRelic(name, characterNames);
        return ResponseEntity.ok(updatedRelic);
    }

    // Получение артефакта с его персонажами по имени
    @GetMapping("/{name}/characters")
    public ResponseEntity<RelicWithCharactersDto> getRelicWithCharacters(@PathVariable
                                                                             String name) {
        RelicWithCharactersDto relicWithCharacters = relicsService.getRelicWithCharacters(name);
        return ResponseEntity.ok(relicWithCharacters);
    }
}

