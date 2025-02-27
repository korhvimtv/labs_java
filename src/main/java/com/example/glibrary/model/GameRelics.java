package com.example.glibrary.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameRelics {

    @JsonFilter("rName")
    private String rName;
    @JsonFilter("rType")
    private String rType;
    @JsonFilter("r2pcs")
    private String r2pcs;
    @JsonFilter("r4pcs")
    private String r4pcs;
    @JsonFilter("rRarity")
    private GameRarity rRarity;

    public GameRelics(String rName, String rType, String r2pcs, String r4psc, GameRarity rRarity) {
        this.rName = rName;
        this.rType = rType;
        this.r2pcs = r2pcs;
        this.r4pcs = r4psc;
        this.rRarity = rRarity;
    }
}
