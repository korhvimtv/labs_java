package com.example.glibrary.DTO;

import java.util.Set;

public class RelicWithCharactersDto {
    private String relicName;
    private Set<String> characterNames;

    // Getters и Setters
    public String getRelicName() {
        return relicName;
    }

    public void setRelicName(String relicName) {
        this.relicName = relicName;
    }

    public Set<String> getCharacterNames() {
        return characterNames;
    }

    public void setCharacterNames(Set<String> characterNames) {
        this.characterNames = characterNames;
    }

}
