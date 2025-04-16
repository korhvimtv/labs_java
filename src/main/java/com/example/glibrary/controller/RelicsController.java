package com.example.glibrary.controller;

import com.example.glibrary.DTO.RelicsDto;
import com.example.glibrary.model.Relic;
import com.example.glibrary.service.RelicsService;
import java.util.List;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SuppressWarnings("checkstyle:Indentation")
@RestController
@RequestMapping("/relics")
public class RelicsController {

    private final RelicsService relicsService;

    public RelicsController(RelicsService relicsService) {

        this.relicsService = relicsService;
    }

    @PostMapping
    public ResponseEntity<Relic> createRelic(@RequestBody RelicsDto relicsDto) {

        Relic createdRelic = relicsService.createRelic(relicsDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRelic);
   }

    @GetMapping
    public ResponseEntity<List<RelicsDto>> readRelics() {

        List<RelicsDto> relics = relicsService.readRelics();
        return ResponseEntity.status(HttpStatus.OK).body(relics);
    }

    @GetMapping("/{name}")
    public ResponseEntity<RelicsDto> readRelicsByName(@PathVariable String name) {

        return relicsService.readRelicsByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{name}")
    public ResponseEntity<Relic> updateRelic(@PathVariable String name, @RequestBody RelicsDto relicsDto) {

        Relic updatedRelic = relicsService.updateRelic(name, relicsDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedRelic);
    }

    @PatchMapping("/{RelicName}/add/{CharacterName}")
    public ResponseEntity<Relic> updateRelicCharacter(@PathVariable String RelicName, @PathVariable String CharacterName) {

        Relic updatedRelic = relicsService.updateRelicCharacter(RelicName, CharacterName);
        return ResponseEntity.status(HttpStatus.OK).body(updatedRelic);
    }

    @Transactional
    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteRelic(@PathVariable String name) {
        relicsService.deleteRelic(name);
        return ResponseEntity.noContent().build(); // Возвращаем корректный HTTP статус 204
    }
}

