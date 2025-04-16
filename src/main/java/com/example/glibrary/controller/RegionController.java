package com.example.glibrary.controller;

import com.example.glibrary.DTO.RegionDto;
import com.example.glibrary.model.Region;
import com.example.glibrary.service.RegionService;
import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/regions")
public class RegionController {

    private final RegionService regionService;

    public RegionController(RegionService regionService) {

        this.regionService = regionService;
    }

    @PostMapping
    public ResponseEntity<Region> createRegion(@RequestBody RegionDto regionDto) {

        Region createdRegion = regionService.createRegion(regionDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRegion);
    }

    @GetMapping
    public ResponseEntity<List<RegionDto>> readRegions() {

        List<RegionDto> regions = regionService.readRegions();
        return ResponseEntity.status(HttpStatus.OK).body(regions);
    }

    @GetMapping("/{name}")
    public ResponseEntity<RegionDto> readRegionByName(@PathVariable String name) {

        return regionService.readRegionByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{region}/characters")

    @PutMapping("/{name}")
    public ResponseEntity<Region> updateRegion(@PathVariable String name, @RequestBody RegionDto regionDto) {

        Region updatedRegion = regionService.updateRegion(name, regionDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedRegion);
    }

    @PatchMapping("/{regionName}/add/{characterName}")
    public ResponseEntity<RegionDto> updateRegionCharacter(@PathVariable String characterName, @PathVariable String regionName) {
        RegionDto updatedRegion = regionService.updateRegionCharacter(characterName, regionName);
        return ResponseEntity.status(HttpStatus.OK).body(updatedRegion);
    }

    @Transactional
    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteRegion(@PathVariable String name) {
        regionService.deleteRegion(name);
        return ResponseEntity.noContent().build();
    }
}

