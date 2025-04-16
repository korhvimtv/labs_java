package com.example.glibrary.controller;

import com.example.glibrary.DTO.CharacterDto;
import com.example.glibrary.model.Character;
import com.example.glibrary.service.CharacterService;
import java.util.List;

import com.example.glibrary.service.VisitCounterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/characters")
@Tag(name = "Characters", description = "Operations with Characters")
public class CharacterController {

    private final CharacterService characterService;
    private final VisitCounterService visitCounterService;

    public CharacterController(CharacterService characterService, VisitCounterService visitCounterService) {

        this.characterService = characterService;
        this.visitCounterService = visitCounterService;
    }

    @PostMapping
    @Operation(summary = "Create Character")
    public ResponseEntity<Character> createCharacter(@Valid @RequestBody CharacterDto characterDto) {

        Character createdCharacter = characterService.createCharacter(characterDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCharacter);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<CharacterDto>> createBulkCharacters(@RequestBody List<CharacterDto> characterDtos) {
        List<CharacterDto> createdCharacters = characterService.createBulkCharacters(characterDtos);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCharacters);
    }

    @GetMapping
    @Operation(summary = "Read all Characters")
    public ResponseEntity<List<CharacterDto>> readCharacters() {

        List<CharacterDto> characters = characterService.readCharacters();
        return ResponseEntity.status(HttpStatus.OK).body(characters);
    }

    @GetMapping("/{name}")
    @Operation(summary = "Read Character by Name")
    public ResponseEntity<CharacterDto> readCharacterByName(@Valid @PathVariable String name) {

        CharacterDto character = characterService.readCharacterByName(name);
        return ResponseEntity.ok(character);
    }

    @GetMapping("/{name}/count")
    @Operation(summary = "Get Visit count for Character")
    public ResponseEntity<String> getCharacterVisitCount(@PathVariable String name) {
        int count = visitCounterService.getCharacterVisitCount(name);
        return ResponseEntity.ok("Character '" + name + "' visited " + count + " times.");
    }

    @GetMapping("/element/role/search")
    @Operation(summary = "Read Characters by Type")
    public ResponseEntity<List<CharacterDto>> readCharacterByType(@Valid @Param("type")
                                                                      @RequestParam String type, @Valid @Param("role")
                                                                  @RequestParam String role) {

        List<CharacterDto> characters = characterService.readCharacterByTypeAndRole(type, role);
        return ResponseEntity.status(HttpStatus.OK).body(characters);
    }

    @GetMapping("/region/{regionName}")
    @Operation(summary = "Read Characters by Region name")
    public ResponseEntity<List<CharacterDto>> findCharactersByRegionName(@Valid @PathVariable String regionName) {
        List<CharacterDto> characters = characterService.findCharactersByRegionName(regionName);
        return ResponseEntity.ok(characters);
    }


    @PutMapping("/{name}")
    @Operation(summary = "Update Character")
    public ResponseEntity<Character> updateCharacter(@Valid @PathVariable String name,
                                                     @RequestBody CharacterDto characterDto) {

        Character updatedCharacter = characterService.updateCharacter(name, characterDto);
        return ResponseEntity.ok(updatedCharacter);
    }

    @Transactional
    @DeleteMapping("/{name}")
    @Operation(summary = "Delete Character")
    public ResponseEntity<Void> deleteCharacter(@Valid @PathVariable String name) {
        characterService.deleteCharacter(name);
        return ResponseEntity.noContent().build();
    }
}