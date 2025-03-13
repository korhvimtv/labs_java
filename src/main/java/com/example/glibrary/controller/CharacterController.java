package com.example.glibrary.controller;

import com.example.glibrary.model.GameCharacter;
import com.example.glibrary.service.CharacterService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/characters")
public class CharacterController {

    private final CharacterService characterService;

    @Autowired
    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    @PostMapping
    public GameCharacter createCharacter(@RequestBody GameCharacter character) {
        return characterService.createCharacter(character);
    }

    @GetMapping
    public List<GameCharacter> readCharacters() {
        return characterService.readCharacters();
    }

    @GetMapping("/{name}")
    public GameCharacter readCharacter(@PathVariable String name) {
        return characterService.readCharacter(name);
    }

    @PutMapping("/{name}")
    public GameCharacter updateCharacter(@PathVariable String name, @RequestBody GameCharacter character) {
        return characterService.updateCharacter(name, character);
    }

    @DeleteMapping("/{name}")
    public void deleteCharacter(@PathVariable String name) {
        characterService.deleteCharacter(name);
    }

}