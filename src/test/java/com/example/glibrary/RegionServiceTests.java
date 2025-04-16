package com.example.glibrary;

import com.example.glibrary.DTO.CharacterDto;
import com.example.glibrary.DTO.RegionDto;
import com.example.glibrary.exception.NotFoundException;
import com.example.glibrary.model.Character;
import com.example.glibrary.model.Region;
import com.example.glibrary.repository.CharacterRepository;
import com.example.glibrary.repository.RegionRepository;
import com.example.glibrary.service.RegionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegionServiceTest {

    private RegionRepository regionRepository;
    private CharacterRepository characterRepository;
    private RegionService regionService;

    @BeforeEach
    void setUp() {
        regionRepository = mock(RegionRepository.class);
        characterRepository = mock(CharacterRepository.class);
        regionService = new RegionService(regionRepository, characterRepository);
    }

    @Test
    void testCreateRegion() {
        RegionDto dto = new RegionDto();
        dto.setName("Inazuma");
        dto.setArchon("Raiden");

        Region savedRegion = new Region();
        savedRegion.setName("Inazuma");
        savedRegion.setArchon("Raiden");

        when(regionRepository.save(any(Region.class))).thenReturn(savedRegion);

        Region result = regionService.createRegion(dto);

        assertEquals("Inazuma", result.getName());
        assertEquals("Raiden", result.getArchon());
    }

    @Test
    void testReadRegionsSuccess() {
        Region region = new Region();
        region.setId(1L);
        region.setName("Liyue");
        region.setArchon("Zhongli");

        when(regionRepository.findAll()).thenReturn(List.of(region));

        List<RegionDto> result = regionService.readRegions();

        assertEquals(1, result.size());
        assertEquals("Liyue", result.get(0).getName());
    }

    @Test
    void testReadRegionsEmptyThrowsException() {
        when(regionRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () -> regionService.readRegions());
    }

    @Test
    void testReadRegionByNameUsesCache() {
        Region region = new Region();
        region.setName("Sumeru");
        region.setArchon("Kusanali");

        when(regionRepository.findByName("Sumeru")).thenReturn(Optional.of(region));

        // Первый вызов — попадание в репозиторий
        RegionDto firstCall = regionService.readRegionByName("Sumeru");

        // Второй вызов — должен использовать кэш (репозиторий не будет вызываться)
        RegionDto secondCall = regionService.readRegionByName("Sumeru");

        assertEquals(firstCall.getName(), secondCall.getName());

        // Репозиторий должен быть вызван только один раз
        verify(regionRepository, times(1)).findByName("Sumeru");
    }

    @Test
    void testReadRegionByNameNotFound() {
        when(regionRepository.findByName("Mondstadt")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> regionService.readRegionByName("Mondstadt"));
    }

    @Test
    void testUpdateRegionSuccess() {
        Region region = new Region();
        region.setName("Old");

        RegionDto update = new RegionDto();
        update.setName("New");
        update.setArchon("Venti");

        when(regionRepository.findByName("Old")).thenReturn(Optional.of(region));
        when(regionRepository.save(any())).thenReturn(region);

        Region result = regionService.updateRegion("Old", update);

        assertEquals("New", result.getName());
        assertEquals("Venti", result.getArchon());
    }

    @Test
    void testUpdateRegionCharacterSuccess() {
        Region region = new Region();
        region.setName("Fontaine");
        region.setCharacters(new HashSet<>());

        Character character = new Character();
        character.setName("Neuvillette");

        when(characterRepository.findByName("Neuvillette")).thenReturn(Optional.of(character));
        when(regionRepository.findByName("Fontaine")).thenReturn(Optional.of(region));

        RegionDto result = regionService.updateRegionCharacter("Neuvillette", "Fontaine");

        assertEquals("Fontaine", result.getName());
        verify(characterRepository).save(character);
        verify(regionRepository).save(region);
        assertEquals(region, character.getRegion());
        assertTrue(region.getCharacters().contains(character));
    }

    @Test
    void testUpdateRegionCharacterRegionNotFound() {
        when(characterRepository.findByName(anyString())).thenReturn(Optional.of(new Character()));
        when(regionRepository.findByName(anyString())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> regionService.updateRegionCharacter("Aether", "UnknownRegion"));
    }

    @Test
    void testDeleteRegionSuccess() {
        Region region = new Region();
        region.setName("Natlan");

        Character character = new Character();
        character.setName("Diluc");
        character.setRegion(region);

        region.setCharacters(new HashSet<>(Set.of(character)));

        when(regionRepository.findByName("Natlan")).thenReturn(Optional.of(region));

        regionService.deleteRegion("Natlan");

        assertNull(character.getRegion());
        verify(characterRepository).save(character);
        verify(regionRepository).delete(region);
    }

    @Test
    void testDeleteRegionNotFound() {
        when(regionRepository.findByName("Unknown")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> regionService.deleteRegion("Unknown"));
    }
}
