package com.example.glibrary.DTO;

import com.example.glibrary.model.Rarity;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CharacterDto {

    private Long id;

    @NotBlank(message = "Character Name Cant be NULL")
    private String name;

    @NotBlank(message = "Character Type Cant be NULL")
    private String type;

    @NotBlank(message = "Character Role Cant be NULL")
    private String role;

    @NotBlank(message = "Character Weapon Cant be NULL")
    private String weapon;

    @NotNull(message = "Character Rarity Cant be NULL")
    private Rarity rarity;

    private Set<RelicsDto> relic;
    private Set<RegionDto> region;
}
