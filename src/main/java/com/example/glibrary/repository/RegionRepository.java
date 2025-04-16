package com.example.glibrary.repository;

import com.example.glibrary.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {

    Optional<Region> findByName(String regionName);

    void deleteByName(String regionName);
}