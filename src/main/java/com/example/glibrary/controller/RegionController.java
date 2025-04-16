package com.example.glibrary.controller;

import com.example.glibrary.DTO.RegionDto;
import com.example.glibrary.model.Region;
import com.example.glibrary.repository.RegionRepository;
import com.example.glibrary.service.RegionService;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/regions")
@Tag(name = "Regions", description = "Operations with Regions")
public class RegionController {

    private final RegionService regionService;

    public RegionController(RegionService regionService) {

        this.regionService = regionService;
    }

    @PostMapping
    @Operation(summary = "Create Region")
    public ResponseEntity<Region> createRegion(@RequestBody RegionDto regionDto) {

        Region createdRegion = regionService.createRegion(regionDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRegion);
    }

    @GetMapping
    @Operation(summary = "Read all Regions")
    public ResponseEntity<List<RegionDto>> readRegions() {

        List<RegionDto> regions = regionService.readRegions();
        return ResponseEntity.status(HttpStatus.OK).body(regions);
    }

    @GetMapping("/{name}")
    @Operation(summary = "Read Region by Name")
    public ResponseEntity<RegionDto> readRegionByName(@PathVariable String name) {

        RegionDto region = regionService.readRegionByName(name);
        return ResponseEntity.ok(region);
    }

    @PutMapping("/{name}")
    @Operation(summary = "Update Region")
    public ResponseEntity<Region> updateRegion(@PathVariable String name, @RequestBody RegionDto regionDto) {

        Region updatedRegion = regionService.updateRegion(name, regionDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedRegion);
    }

    @PatchMapping("/{regionName}/add/{characterName}")
    @Operation(summary = "Add Character to Region")
    public ResponseEntity<RegionDto> updateRegionCharacter(@PathVariable String characterName, @PathVariable String regionName) {
        RegionDto updatedRegion = regionService.updateRegionCharacter(characterName, regionName);
        return ResponseEntity.status(HttpStatus.OK).body(updatedRegion);
    }

    @Transactional
    @DeleteMapping("/{name}")
    @Operation(summary = "Delete Region")
    public ResponseEntity<Void> deleteRegion(@PathVariable String name) {
        regionService.deleteRegion(name);
        return ResponseEntity.noContent().build();
    }
}

