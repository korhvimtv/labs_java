package com.example.glibrary.controller;

import com.example.glibrary.DTO.CharacterDto;
import com.example.glibrary.DTO.CharacterWithRelicsDto;
import com.example.glibrary.service.CharacterService;
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
@RequestMapping("/characters")
public class CharacterController {

    private final CharacterService characterService;

    @Autowired
    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    @PostMapping
    public ResponseEntity<CharacterDto> createCharacter(@RequestBody CharacterDto characterDto) {
        CharacterDto created = characterService.createCharacter(characterDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PostMapping("/{name}/relics")
    public ResponseEntity<CharacterDto> addRelicsToCharacter(
            @PathVariable String name,
            @RequestBody Set<Long> relicIds) {

        CharacterDto updatedCharacter = characterService.addRelicsToCharacter(name, relicIds);

        return ResponseEntity.ok(updatedCharacter);
    }

    @GetMapping
    public ResponseEntity<List<CharacterDto>> readCharacters() {
        List<CharacterDto> characters = characterService.readCharacters();
        return ResponseEntity.ok(characters);
    }

    @GetMapping("/{name}")
    public ResponseEntity<CharacterDto> readCharacter(@PathVariable String name) {
        CharacterDto character = characterService.readCharacter(name);
        return ResponseEntity.ok(character);
    }

    @GetMapping("/{name}/relics")
    public ResponseEntity<CharacterWithRelicsDto> getCharacterWithRelics(@PathVariable
                                                                             String name) {
        CharacterWithRelicsDto characterWithRelics = characterService.getCharacterWithRelics(name);
        return ResponseEntity.ok(characterWithRelics);
    }

    @PutMapping("/{name}")
    public ResponseEntity<CharacterDto> updateCharacter(
            @PathVariable String name,
            @RequestBody CharacterDto characterDto) {
        CharacterDto updated = characterService.updateCharacter(name, characterDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteCharacter(@PathVariable String name) {
        characterService.deleteCharacter(name);
        return ResponseEntity.noContent().build();
    }
}