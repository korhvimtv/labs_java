package com.example.glibrary.DTO;

import com.example.glibrary.model.Rarity;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RelicsDto {

    private Long id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("type")
    private String type;
    @JsonProperty("pcs2")
    private String pcs2;
    @JsonProperty("pcs4")
    private String pcs4;
    @JsonProperty("rarity")
    private Rarity rarity;
    @JsonProperty("characterId")
    private Set<Long> characterId = new HashSet<>();
}
