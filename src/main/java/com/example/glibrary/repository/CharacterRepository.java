package com.example.glibrary.repository;

import com.example.glibrary.model.GameCharacter;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class CharacterRepository {

    List<GameCharacter> characters = new ArrayList<>();
    Scanner in = new Scanner(System.in);

    public CharacterRepository() {
        System.out.println("Welcome to GLibrary Repository\nHow many characters will be added?\n > ");
        int counter = in.nextInt();
        in.nextLine();

        for (int i = 0; i < counter; i++) {
            System.out.println("Enter data for" + (i + 1) + "character:");

            System.out.print("Name: ");
            String charName = in.nextLine();

            System.out.print("Type: ");
            String charType = in.nextLine();

            System.out.print("Role: ");
            String charRole = in.nextLine();

            System.out.print("Weapon: ");
            String charWeapon = in.nextLine();

            characters.add(new GameCharacter(charName, charType, charRole, charWeapon));
        }

        System.out.println("Characters added successfully!");
    }
}
