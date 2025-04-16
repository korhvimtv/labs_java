package com.example.glibrary.controller;

import com.example.glibrary.DTO.CharacterDto;
import com.example.glibrary.model.Character;
import com.example.glibrary.service.CharacterService;
import java.util.List;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/characters")
public class CharacterController {

    private final CharacterService characterService;

    public CharacterController(CharacterService characterService) {

        this.characterService = characterService;
    }

    @PostMapping
    public ResponseEntity<Character> createCharacter(@RequestBody CharacterDto characterDto) {

        Character createdCharacter = characterService.createCharacter(characterDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCharacter);
    }

    @GetMapping
    public ResponseEntity<List<CharacterDto>> readCharacters() {

        List<CharacterDto> characters = characterService.readCharacters();
        return ResponseEntity.status(HttpStatus.OK).body(characters);
    }

    @GetMapping("/{name}")
    public ResponseEntity<CharacterDto> readCharacterByName(@PathVariable String name) {

        return characterService.readCharacterByName(name)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/element/role/search")
    public ResponseEntity<List<CharacterDto>> readCharacterByType(@Param("type")
                                                                      @RequestParam String type, @Param("role")
                                                                  @RequestParam String role) {

        List<CharacterDto> characters = characterService.readCharacterByTypeAndRole(type, role);
        return ResponseEntity.status(HttpStatus.OK).body(characters);
    }

    @GetMapping("/region/{regionName}")
    public ResponseEntity<List<CharacterDto>> findCharactersByRegionName(@PathVariable String regionName) {
        List<CharacterDto> characters = characterService.findCharactersByRegionName(regionName);
        return ResponseEntity.ok(characters);
    }


    @PutMapping("/{name}")
    public ResponseEntity<Character> updateCharacter(@PathVariable String name,
                                                     @RequestBody CharacterDto characterDto) {

        Character updatedCharacter = characterService.updateCharacter(name, characterDto);
        return ResponseEntity.ok(updatedCharacter);
    }

    @Transactional
    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteCharacter(@PathVariable String name) {
        characterService.deleteCharacter(name);
        return ResponseEntity.noContent().build();
    }
}