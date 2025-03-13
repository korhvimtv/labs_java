package com.example.glibrary.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@Getter
@Setter
@Entity
public class GameCharacter {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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

    public GameCharacter () {}
}
