package com.example.glibrary.repository;

import com.example.glibrary.model.GameCharacter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterRepository extends JpaRepository<GameCharacter, Long> {
    Optional<GameCharacter> findByName(String name);

    List<GameCharacter> findByNameIn(Set<String> names);
}
