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
public class Relic {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String type;
    private String pcs2;
    private String pcs4;
    private Rarity rarity;

    @ManyToMany(mappedBy = "recRelics", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Character> characters = new HashSet<>();

    public Relic() {}
}
