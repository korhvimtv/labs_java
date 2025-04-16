package com.example.glibrary.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.example.glibrary.model.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {

    Optional<Character> findByName(String name);

    Optional<Character> deleteByName(String name);

    List<Character> findByNameIn(Set<String> names);

    List<Character> findByType(String type);

    List<Character> findByTypeAndRole(String type, String role);

    @Query(value = "SELECT c.* FROM character c JOIN region r ON c.region_id = r.id WHERE r.name = :regionName", nativeQuery = true)
    List<Character> findByRegionName(@Param("regionName") String regionName);

}
