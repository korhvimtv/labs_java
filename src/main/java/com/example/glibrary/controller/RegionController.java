package com.example.glibrary.controller;

import com.example.glibrary.DTO.CharacterDto;
import com.example.glibrary.DTO.RegionDto;
import com.example.glibrary.service.RegionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/regions")
public class RegionController {

    private final RegionService regionService;

    @Autowired
    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    @PostMapping
    public ResponseEntity<RegionDto> createRegion(@RequestBody RegionDto regionDto) {
        RegionDto created = regionService.createRegion(regionDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegionDto> updateRegion(@PathVariable Long id,
                                                  @RequestBody RegionDto regionDto) {
        RegionDto updatedRegion = regionService.updateRegion(id, regionDto);
        return ResponseEntity.ok(updatedRegion);
    }

    @GetMapping
    public ResponseEntity<List<RegionDto>> readRegions() {
        List<RegionDto> regions = regionService.readRegions();
        return ResponseEntity.ok(regions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegionDto> readRegion(@PathVariable Long id) {
        RegionDto region = regionService.readRegion(id);
        return ResponseEntity.ok(region);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegion(@PathVariable Long id) {
        regionService.deleteRegion(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{characterId}/assign/{regionId}")
    public ResponseEntity<CharacterDto> assignCharacterToRegion(
            @PathVariable Long characterId,
            @PathVariable Long regionId) {
        CharacterDto updatedCharacter = regionService
                .assignCharacterToRegion(characterId, regionId);
        return ResponseEntity.ok(updatedCharacter);
    }
}
