package com.example.glibrary.DTO;

import com.example.glibrary.model.Rarity;
import java.util.Set;
import lombok.Data;

@Data
public class CharacterDto {

    private Long id;
    private String name;
    private String type;
    private String role;
    private String weapon;
    private Rarity rarity;
    private Set<RelicsDto> relic;
    private Set<RegionDto> region;
}
