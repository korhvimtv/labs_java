package com.example.glibrary.DTO;

import java.util.Set;
import lombok.Data;

@Data
public class CharacterWithRelicsDto {
    private String characterName;
    private Set<String> relicNames;

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public Set<String> getRelicNames() {
        return relicNames;
    }

    public void setRelicNames(Set<String> relicNames) {
        this.relicNames = relicNames;
    }
}
