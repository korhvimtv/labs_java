package com.example.glibrary.repository;

import com.example.glibrary.model.GameCharacter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import com.example.glibrary.model.GameRarity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterRepository extends JpaRepository<GameCharacter, Long> {

    Optional<GameCharacter> findByName(String name);

    Optional<GameCharacter> deleteByName(String name);

    List<GameCharacter> findByNameIn(Set<String> names);
}
