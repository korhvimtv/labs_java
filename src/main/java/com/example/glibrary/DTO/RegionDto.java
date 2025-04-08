package com.example.glibrary.DTO;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegionDto {
    private Long id;
    private String regionName;
    private String archonRegion;
    private Set<CharacterDto> characters;
}
