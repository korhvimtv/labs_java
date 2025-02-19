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

    public GameCharacter(String name, String type, String role, String weapon) {
        this.name = name;
        this.type = type;
        this.role = role;
        this.weapon = weapon;
    }
}
