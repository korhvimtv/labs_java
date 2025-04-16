package com.example.glibrary.DTO;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class RegionDto {

    private Long id;

    @NotBlank(message = "Region Name Cant be NULL")
    private String name;

    @NotBlank(message = "Region Archon Cant be NULL")
    private String archon;

    private Set<CharacterDto> characters;
}
