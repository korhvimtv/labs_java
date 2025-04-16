package com.example.glibrary.repository;

import com.example.glibrary.model.Relic;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelicsRepository extends JpaRepository<Relic, Long> {

    Optional<Relic> findByName(String name);

    void deleteByName(String relicsName);
}
