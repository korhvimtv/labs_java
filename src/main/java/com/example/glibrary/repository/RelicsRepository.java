package com.example.glibrary.repository;

import com.example.glibrary.model.GameRelics;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelicsRepository extends JpaRepository<GameRelics, Long> {
    Optional<GameRelics> findByrName(String name);
}
