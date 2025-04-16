package com.example.glibrary.DTO;

import com.example.glibrary.model.Rarity;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RelicsDto {

    private Long id;

    @NotBlank(message = "Relic Name Cant be NULL")
    private String name;

    @NotBlank(message = "Relic Type Cant be NULL")
    private String type;

    @NotBlank(message = "Relic Bonus Cant be NULL")
    private String pcs2;

    @NotBlank(message = "Relic Bonus Cant be NULL")
    private String pcs4;

    @NotNull(message = "Relic Rarity Cant be NULL")
    private Rarity rarity;

    private Set<Long> characterId = new HashSet<>();
}
