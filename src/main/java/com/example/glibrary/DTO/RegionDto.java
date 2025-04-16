package com.example.glibrary.DTO;

import java.util.Set;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class RegionDto {

    private Long id;
    private String name;
    private String archon;
    private Set<CharacterDto> characters;
}
