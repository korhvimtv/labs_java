package com.example.glibrary.controller;

import com.example.glibrary.model.GameCharacter;
import com.example.glibrary.service.CharacterService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public boolean addCharacter(@RequestBody GameCharacter character) {
        return characterService.addCharacter(character);
    }

    @GetMapping
    public List<GameCharacter> getAllCharacters() {
        return characterService.getAllCharacters();
    }

    @GetMapping("/{name}")
    public List<GameCharacter> getCharactersByName(@PathVariable String name) {
        return characterService.getCharactersByName(name);
    }

    @GetMapping("/search")
    public List<GameCharacter> getCharacterByWeaponAndType(@RequestParam String weapon,
                                                           @RequestParam String type) {
        return characterService.getCharacterByWeaponAndType(weapon, type);
    }
}