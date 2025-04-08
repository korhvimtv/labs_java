package com.example.glibrary.repository;

import com.example.glibrary.model.GameRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<GameRegion, Long> {

    Optional<GameRegion> findByRegionName(String regionName);

    void deleteByRegionName(String regionName);
}