package com.example.glibrary.DTO;

import com.example.glibrary.model.GameRarity;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;

@Data
public class RelicsDto {

    private Long id;
    @JsonProperty("rName")
    private String rName;
    @JsonProperty("rType")
    private String rType;
    @JsonProperty("r2pcs")
    private String r2pcs;
    @JsonProperty("r4pcs")
    private String r4pcs;
    @JsonProperty("rRarity")
    private GameRarity rRarity;
    @JsonProperty("characterIds")
    private Set<Long> characterIds = new HashSet<>();
}
