package com.example.glibrary.repository;

import com.example.glibrary.model.GameRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepository extends JpaRepository<GameRegion, Long> {}