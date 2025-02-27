package com.example.glibrary.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameCharacter {

    @JsonProperty("name")
    private String name;
    @JsonProperty("type")
    private String type;
    @JsonProperty("role")
    private String role;
    @JsonProperty("weapon")
    private String weapon;
    @JsonProperty("rarity")
    private GameRarity rarity;

    public GameCharacter(String name, String type, String role, String weapon, GameRarity rarity) {
        this.name = name;
        this.type = type;
        this.role = role;
        this.weapon = weapon;
        this.rarity = rarity;
    }
}
