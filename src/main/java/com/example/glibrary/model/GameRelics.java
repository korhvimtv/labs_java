package com.example.glibrary.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class GameRelics {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String rName;
    private String rType;
    private String r2pcs;
    private String r4pcs;
    private GameRarity rRarity;

    @ManyToMany(mappedBy = "recRelics", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<GameCharacter> characters = new HashSet<>();

    public GameRelics() {}
}
