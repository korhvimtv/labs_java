package com.example.glibrary.model;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameCharacter {

    private String name;
    private String type;
    private String role;
    private String weapon;

    public GameCharacter() {}

    public GameCharacter(String name, String type, String role, String weapon) {
        this.name = name;
        this.type = type;
        this.role = role;
        this.weapon = weapon;
    }
}
