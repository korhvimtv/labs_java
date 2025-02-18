package com.example.glibrary.model;

public class Groups extends GameCharacter {

    private String[] groups = new String[4];

    public Groups() {};
    public Groups(String[] groups) {
        super();
        this.groups = groups;
    }

    public String[] getGroups() {
        return groups;
    }
}
