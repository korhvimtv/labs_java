package com.example.glibrary;

import com.example.glibrary.model.GameCharacter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.glibrary.service.CharacterService;

import java.util.List;

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

    @GetMapping("/search")
    public List<GameCharacter> getCharactersByName(@RequestParam String name) {
        return characterService.getCharactersByName(name);
    }

    @GetMapping("/{type}/{weapon}")
    public List<GameCharacter> getCharacterByWeaponAndType(@PathVariable String weapon, @PathVariable String type) {
        return characterService.getCharacterByWeaponAndType(weapon, type);
    }
}