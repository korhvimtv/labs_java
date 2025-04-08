package com.example.glibrary.DTO;

import com.example.glibrary.model.GameRarity;
import java.util.Set;
import lombok.Data;

@Data
public class CharacterDto {
    private Long id;
    private String name;
    private String type;
    private String role;
    private String weapon;
    private GameRarity rarity;
    private Set<Long> relicIds;
}
